package com.bs23.streamchat.message_app.presentation.login

import com.bs23.streamchat.BuildConfig
import com.bs23.streamchat.core.presentation.base.BaseUiState
import com.bs23.streamchat.core.presentation.base.MVIViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.models.User

class LoginViewModel(
    private val client: ChatClient,
) : MVIViewModel<BaseUiState<LoginScreenUiState>, LoginScreenEvent>() {

    private var _uiState = LoginScreenUiState()

    init {
        setState(BaseUiState.Data(_uiState))
    }

    override fun onTriggerEvent(eventType: LoginScreenEvent) {
        when (eventType) {
            LoginScreenEvent.LoginAsGuestClicked -> loginAsGuest()
            LoginScreenEvent.LoginButtonClicked -> loginAsUser()
            is LoginScreenEvent.UserNameChanged -> {
                _uiState = _uiState.copy(
                    userName = eventType.userName
                )
                setState(BaseUiState.Data(_uiState))
            }
        }
    }

    private fun loginAsGuest() = safeLaunch {
        client.connectGuestUser(
            userId = _uiState.userName,
            username = _uiState.userName
        ).enqueue { result ->
            if (result.isSuccess) {
                _uiState = _uiState.copy(
                    isLoading = false,
                    isLoggedIn = true
                )
                setState(BaseUiState.Data(_uiState))
            } else {
                _uiState = _uiState.copy(
                    isLoading = false,
                    isError = true,
                    errorMessage = result.errorOrNull()?.message ?: "Unknown error"
                )
                setState(BaseUiState.Error(Throwable(message = _uiState.errorMessage)))
            }
        }
    }

    private fun loginAsUser() = safeLaunch {
        client.connectUser(
            user = User(name = _uiState.userName, id = _uiState.userName),
            token = BuildConfig.TOKEN
        ).enqueue { result ->
            if (result.isSuccess) {
                _uiState = _uiState.copy(
                    isLoading = false,
                    isLoggedIn = true
                )
                setState(BaseUiState.Data(_uiState))
            } else {
                _uiState = _uiState.copy(
                    isLoading = false,
                    isError = true,
                    errorMessage = result.errorOrNull()?.message ?: "Unknown error"
                )
                setState(BaseUiState.Error(Throwable(message = _uiState.errorMessage)))
            }
        }
    }
}