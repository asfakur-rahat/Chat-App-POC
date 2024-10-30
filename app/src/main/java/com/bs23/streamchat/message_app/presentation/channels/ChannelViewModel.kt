package com.bs23.streamchat.message_app.presentation.channels

import androidx.lifecycle.viewModelScope
import com.bs23.streamchat.core.presentation.base.BaseUiState
import com.bs23.streamchat.core.presentation.base.MVIViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.api.models.QueryChannelsRequest
import io.getstream.chat.android.client.api.models.QueryUsersRequest
import io.getstream.chat.android.models.Filters
import io.getstream.chat.android.models.User
import io.getstream.chat.android.models.querysort.QuerySortByField
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID

class ChannelViewModel(
    private val client: ChatClient,
): MVIViewModel<BaseUiState<ChannelListScreenUiState>, ChannelListScreenEvent, ChannelListScreenEffect>() {

    private var _uiState = ChannelListScreenUiState()

    init {
        setState(BaseUiState.Data(_uiState))
    }

    override fun onTriggerEvent(eventType: ChannelListScreenEvent) {
        when(eventType){
            is ChannelListScreenEvent.OnCreateNewChannel -> createNewChannel(eventType.channelName)

            ChannelListScreenEvent.OnClickCreateNewChannel -> toogleDialog(true)

            is ChannelListScreenEvent.SetCurrentUser -> setCurrentUsername(eventType.username)

            ChannelListScreenEvent.DisMissDialog -> toogleDialog(false)

            ChannelListScreenEvent.OnClickLogout -> logOut()

            is ChannelListScreenEvent.OnQueryChange -> onChangeSearchQuery(eventType.newQuery)

            is ChannelListScreenEvent.OnSearch -> onSearch(query = eventType.query)

            is ChannelListScreenEvent.OnClickSearchedUser -> queryChannel(eventType.user)

            is ChannelListScreenEvent.StartConversation -> startConversation(eventType.user)

            ChannelListScreenEvent.StopShowingUserInformation -> stopShowingUserInformation()

            is ChannelListScreenEvent.NavigateToChannel -> navigateToChannel(eventType.channelId, eventType.channelName)
        }
    }

    private fun navigateToChannel(channelId: String, channelName: String){
        _uiState = _uiState.copy(
            query = "",
            users = emptyList(),
            shouldShowUserInformation = null
        )
        viewModelScope.launch {
            setState(BaseUiState.Data(_uiState))
            setEffect(ChannelListScreenEffect.NavigateToChannel(channelId, channelName))
        }
    }

    private fun onChangeSearchQuery(newQuery: String){
        _uiState = _uiState.copy(query = newQuery)
        setState(BaseUiState.Data(_uiState))
    }

    private var searchJob: Job? = null
    private fun onSearch(query: String) {

        if(query.isBlank() || query.isEmpty()){
            searchJob?.cancel()
            _uiState = _uiState.copy(
                users = emptyList()
            )
            setState(BaseUiState.Data(_uiState))
            return
        }
        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            delay(1000)
            val request = QueryUsersRequest(
                filter = Filters.and(
                    Filters.autocomplete("name", query),
                ),
                offset = 0,
                limit = 10
            )
            client.queryUsers(request)
                .enqueue { result ->
                    result.onSuccess { list ->
                        println(list)
                        _uiState = _uiState.copy(
                            users = list
                        )
                        setState(BaseUiState.Data(_uiState))
                    }.onError { error ->
                        println(error)
                    }
                }
        }

    }

    private fun setCurrentUsername(username: String){
        _uiState = _uiState.copy(currentUsername = username)
        setState(BaseUiState.Data(_uiState))
    }

    private fun toogleDialog(show: Boolean){
        _uiState = _uiState.copy(showDialog = show)
        setState(BaseUiState.Data(_uiState))
    }

    private fun logOut(){
        client.disconnect(false).enqueue()
        setEffect(ChannelListScreenEffect.OnLogOut)
    }

    @Suppress("UNUSED")
    private fun queryChannel(user: User) {
        client.getCurrentUser()?.let{ currentUser ->
            val request = QueryChannelsRequest(
                filter = Filters.and(
                    Filters.eq("type", "messaging"),
                    Filters.`in`("members", listOf(currentUser.id)),
                    Filters.`in`("members", listOf(user.id))
                ),
                offset = 0,
                limit = 1,
                querySort = QuerySortByField.descByName("lastMessageAt"),
                memberLimit = 2
            ).apply {
                watch = true
                state = true
            }
            client.queryChannels(request).enqueue { result ->
                result.onSuccess {
                    if(it.isNotEmpty()){
                        val channel = it.first()
                        navigateToChannel(channel.cid, channel.name)
                    }else{
                        _uiState = _uiState.copy(
                            shouldShowUserInformation = user
                        )
                        setState(BaseUiState.Data(_uiState))
                    }
                }.onError {
                    println(it.message)
                }
            }
        }
    }

    private fun startConversation(user: User) = safeLaunch {
        _uiState = _uiState.copy(
            shouldShowUserInformation = null
        )
        client.getCurrentUser()?.id?.let{
            client.channel(channelType = "messaging", channelId = "").create(
                memberIds = listOf(it, user.id),
                extraData = mapOf()
            ).enqueue { result ->
                result.onSuccess { channel ->
                   viewModelScope.launch {
                       setState(BaseUiState.Data(_uiState))
                      setEffect(ChannelListScreenEffect.NavigateToChannel(channel.cid, channel.name))
                   }
                }.onError { error ->
                    println(error.message)
                    setState(BaseUiState.Error(Throwable(error.message)))
                }
            }
        }
    }
    private fun stopShowingUserInformation(){
        _uiState = _uiState.copy(shouldShowUserInformation = null)
        setState(BaseUiState.Data(_uiState))
    }

    private fun createNewChannel(name: String) = safeLaunch {
        val channelType = "messaging"
        val channelId = UUID.randomUUID().toString()
        _uiState = _uiState.copy(showDialog = false)
        setState(BaseUiState.Loading)
        client.createChannel(
            channelType = channelType,
            channelId = channelId,
            memberIds = listOf(
                _uiState.currentUsername
            ),
            extraData = mapOf(
                "name" to name
            )
        ).enqueue { result ->
            if (result.isSuccess) {
                setState(BaseUiState.Data(_uiState))
            } else {
                setState(BaseUiState.Error(Throwable(message = result.errorOrNull()?.message ?: "Unknown Error")))
            }
        }
    }
}