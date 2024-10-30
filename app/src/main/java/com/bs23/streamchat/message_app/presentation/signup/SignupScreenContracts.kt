package com.bs23.streamchat.message_app.presentation.signup

data class SignupScreenUiState(
    val userName: String = "",
    val userNameError: String? = null,
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val confirmPassword: String = "",
    val confirmPasswordError: String? = null,
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val userID: String = ""
)

sealed interface SignupScreenEvent {
    data class UserSignUp(val userName: String, val email: String, val password: String, val confirmPassword: String): SignupScreenEvent
    data class UserNameChanged(val userName: String): SignupScreenEvent
    data class OnEmailChanged(val email: String): SignupScreenEvent
    data class OnPasswordChanged(val password: String): SignupScreenEvent
    data class OnConfirmPasswordChanged(val confirmPassword: String): SignupScreenEvent
}

sealed interface SignupScreenEffect {
    data class NavigateToChannelScreen(val userID: String): SignupScreenEffect
}