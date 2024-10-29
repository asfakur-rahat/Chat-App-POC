package com.bs23.streamchat.message_app.presentation.channels

import com.bs23.streamchat.core.presentation.base.BaseUiState
import com.bs23.streamchat.core.presentation.base.MVIViewModel
import io.getstream.chat.android.client.ChatClient
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
            ChannelListScreenEvent.OnClickCreateNewChannel -> {
                _uiState = _uiState.copy(showDialog = true)
                setState(BaseUiState.Data(_uiState))
            }
            is ChannelListScreenEvent.SetCurrentUser -> {
                _uiState = _uiState.copy(currentUsername = eventType.username)
                setState(BaseUiState.Data(_uiState))
            }

            ChannelListScreenEvent.DisMissDialog -> {
                _uiState = _uiState.copy(showDialog = false)
                setState(BaseUiState.Data(_uiState))
            }

            ChannelListScreenEvent.OnClickLogout -> {
                client.disconnect(false).enqueue()
                _uiState = _uiState.copy(isLoggedIn = false)
                setState(BaseUiState.Data(_uiState))
            }
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