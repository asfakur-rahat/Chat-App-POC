package com.bs23.streamchat.message_app.presentation.channels


data class ChannelListScreenUiState(
    val currentUsername: String = "",
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = true,
    val showDialog: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = "",
//    val channels: List<Channel> = emptyList(),
)

sealed interface ChannelListScreenEvent {
    data object DisMissDialog : ChannelListScreenEvent
    data class SetCurrentUser(val username: String) : ChannelListScreenEvent
    data class OnCreateNewChannel(val channelName: String) : ChannelListScreenEvent
    data object OnClickCreateNewChannel : ChannelListScreenEvent
    data object OnClickLogout : ChannelListScreenEvent
}