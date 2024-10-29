package com.bs23.streamchat.message_app.presentation.login

import androidx.compose.runtime.Immutable

@Immutable
data class LoginScreenUiState(
    val userName: String = "",
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = "",
    val isLoggedIn: Boolean = false
)

sealed interface LoginScreenEvent {
    data class UserNameChanged(val userName: String): LoginScreenEvent
    data object LoginButtonClicked: LoginScreenEvent
    data object LoginAsGuestClicked: LoginScreenEvent
}