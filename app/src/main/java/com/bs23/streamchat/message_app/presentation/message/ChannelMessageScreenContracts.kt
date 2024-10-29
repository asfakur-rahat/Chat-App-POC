package com.bs23.streamchat.message_app.presentation.message

data class ChannelMessageUiState(
    val channelId: String = "",
)

sealed interface ChannelMessageScreenEvent {
    data class SetChannelId(val channelId: String) : ChannelMessageScreenEvent
}