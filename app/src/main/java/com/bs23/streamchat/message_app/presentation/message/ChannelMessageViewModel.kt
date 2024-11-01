package com.bs23.streamchat.message_app.presentation.message

import com.bs23.streamchat.core.presentation.base.BaseUiState
import com.bs23.streamchat.core.presentation.base.MVIViewModel
import com.bs23.streamchat.message_app.presentation.channels.ChannelListScreenEffect
import io.getstream.chat.android.client.ChatClient

class ChannelMessageViewModel(
    private val client: ChatClient
): MVIViewModel<BaseUiState<ChannelMessageUiState>, ChannelMessageScreenEvent, ChannelListScreenEffect>() {

    private var _uiState = ChannelMessageUiState()

    init {
        setState(BaseUiState.Data(_uiState))
    }

    override fun initUiState() {
        _uiState = ChannelMessageUiState()
        setState(BaseUiState.Data(_uiState))
    }

    override fun onTriggerEvent(eventType: ChannelMessageScreenEvent) {
        when(eventType){
            is ChannelMessageScreenEvent.SetChannelId -> {
                _uiState = _uiState.copy(channelId = eventType.channelId)
                setState(BaseUiState.Data(_uiState))
            }
        }
    }
}