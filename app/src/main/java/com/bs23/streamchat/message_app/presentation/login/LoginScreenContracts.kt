package com.bs23.streamchat.message_app.presentation.login

import androidx.compose.runtime.Immutable

@Immutable
data class LoginScreenUiState(
    val userName: String = "",
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = "",
    val isLoggedIn: Boolean = false
)

sealed interface LoginScreenEvent {
    data class UserNameChanged(val userName: String): LoginScreenEvent
    data class EmailChanged(val email: String): LoginScreenEvent
    data class PasswordChanged(val password: String): LoginScreenEvent
    data object LoginButtonClicked: LoginScreenEvent
    data object LoginAsGuestClicked: LoginScreenEvent
}

sealed interface LoginScreenEffect {
    data class NavigateToChannelScreen(val userName: String): LoginScreenEffect
}