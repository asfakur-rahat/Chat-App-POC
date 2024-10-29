package com.bs23.streamchat.message_app.presentation.channels

import com.bs23.streamchat.core.presentation.base.BaseUiState
import com.bs23.streamchat.core.presentation.base.MVIViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.api.models.QueryChannelsRequest
import io.getstream.chat.android.models.Filters
import io.getstream.chat.android.models.querysort.QuerySortByField
import java.util.UUID

class ChannelViewModel(
    private val client: ChatClient,
): MVIViewModel<BaseUiState<ChannelListScreenUiState>, ChannelListScreenEvent>() {

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
        _uiState = _uiState.copy(isLoggedIn = false)
        setState(BaseUiState.Data(_uiState))
    }

    private fun queryChannel(){
        val request = QueryChannelsRequest(
            filter = Filters.and(
                Filters.eq("type", "messaging")
            ),
            offset = 0,
            limit = 10,
            querySort = QuerySortByField.descByName("lastMessageAt")
        ).apply {
            watch = true
            state = true
        }
        client.queryChannels(request).enqueue { result ->

        }
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