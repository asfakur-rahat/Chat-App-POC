package com.bs23.streamchat.message_app.presentation.channels

import io.getstream.chat.android.models.User


data class ChannelListScreenUiState(
    val currentUsername: String = "",
    val isLoading: Boolean = false,
    val showDialog: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = "",
    val query: String = "",
    val users : List<User> = emptyList(),
    val shouldShowUserInformation: User? = null
)

sealed interface ChannelListScreenEvent {
    data object DisMissDialog : ChannelListScreenEvent
    data class SetCurrentUser(val username: String) : ChannelListScreenEvent
    data class OnCreateNewChannel(val channelName: String) : ChannelListScreenEvent
    data object OnClickCreateNewChannel : ChannelListScreenEvent
    data class OnQueryChange(val newQuery: String) : ChannelListScreenEvent
    data class OnSearch(val query: String) : ChannelListScreenEvent
    data class OnClickSearchedUser(val user: User) : ChannelListScreenEvent
    data class StartConversation(val user: User) : ChannelListScreenEvent
    data object StopShowingUserInformation : ChannelListScreenEvent
    data object OnClickLogout : ChannelListScreenEvent
    data class NavigateToChannel(val channelId: String, val channelName: String) : ChannelListScreenEvent
}

sealed interface ChannelListScreenEffect {
    data class NavigateToChannel(val channelId: String, val channelName: String) : ChannelListScreenEffect
    data object OnLogOut : ChannelListScreenEffect
}