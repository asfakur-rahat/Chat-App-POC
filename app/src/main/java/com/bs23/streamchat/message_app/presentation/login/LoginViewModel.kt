package com.bs23.streamchat.message_app.presentation.login

import com.bs23.streamchat.BuildConfig
import com.bs23.streamchat.core.domain.util.onError
import com.bs23.streamchat.core.domain.util.onSuccess
import com.bs23.streamchat.core.presentation.base.BaseUiState
import com.bs23.streamchat.core.presentation.base.MVIViewModel
import com.bs23.streamchat.message_app.domain.ChatAppDataSource
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.models.User

class LoginViewModel(
    private val dataSource: ChatAppDataSource,
    private val client: ChatClient,
) : MVIViewModel<BaseUiState<LoginScreenUiState>, LoginScreenEvent, LoginScreenEffect>() {

    private var _uiState = LoginScreenUiState()

    fun initUiState(){
        _uiState = LoginScreenUiState()
        setState(BaseUiState.Data(_uiState))
    }

    override fun onTriggerEvent(eventType: LoginScreenEvent) {
        when (eventType) {
            LoginScreenEvent.LoginAsGuestClicked -> loginAsGuest()
            LoginScreenEvent.LoginButtonClicked ->  loginAsUserTest() //loginAsUser()
            is LoginScreenEvent.UserNameChanged -> onChangeUserName(eventType.userName)
            is LoginScreenEvent.EmailChanged -> onChangeEmail(eventType.email)
            is LoginScreenEvent.PasswordChanged -> onChangePassword(eventType.password)
        }
    }

    private fun onChangeUserName(userName: String) {
        _uiState = _uiState.copy(
            userName = userName
        )
        setState(BaseUiState.Data(_uiState))
    }

    private fun onChangeEmail(email: String) {
        _uiState = _uiState.copy(
            email = email
        )
        setState(BaseUiState.Data(_uiState))
    }

    private fun onChangePassword(password: String) {
        _uiState = _uiState.copy(
            password = password
        )
        setState(BaseUiState.Data(_uiState))
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

    /**
     * This function is only for development purpose
     */
    @Suppress("UNUSED", "")
    private fun loginAsUserTest() = safeLaunch {
        /**
         * Use This When login As USER User2
         */
        client.connectUser(
            user = User(name = "User2", id = "User2"),
            token = BuildConfig.TOKEN_USER_2
        ).enqueue { result ->
            if (result.isSuccess) {
                _uiState = _uiState.copy(
                    userName = "User2",
                    isLoading = false,
                    isLoggedIn = true
                )
                setState(BaseUiState.Data(_uiState))
                setEffect(LoginScreenEffect.NavigateToChannelScreen(_uiState.userName))
            } else {
                _uiState = _uiState.copy(
                    isLoading = false,
                    isError = true,
                    errorMessage = result.errorOrNull()?.message ?: "Unknown error"
                )
                setState(BaseUiState.Error(Throwable(message = _uiState.errorMessage)))
            }
        }

        /**
         * Use This When login As USER ARRAHAT
         */
//        client.connectUser(
//            user = User(name = "ARRAHAT", id = "ARRAHAT"),
//            token = BuildConfig.TOKEN
//        ).enqueue { result ->
//            if (result.isSuccess) {
//                _uiState = _uiState.copy(
//                    userName = "ARRAHAT",
//                    isLoading = false,
//                    isLoggedIn = true
//                )
//                setState(BaseUiState.Data(_uiState))
//                setEffect(LoginScreenEffect.NavigateToChannelScreen(_uiState.userName))
//            } else {
//                _uiState = _uiState.copy(
//                    isLoading = false,
//                    isError = true,
//                    errorMessage = result.errorOrNull()?.message ?: "Unknown error"
//                )
//                setState(BaseUiState.Error(Throwable(message = _uiState.errorMessage)))
//            }
//        }
    }

    @Suppress("UNUSED", "")
    private fun loginAsUser() = safeLaunch {
        dataSource.signInAndGetToken(_uiState.email, _uiState.password)
            .onSuccess { response ->
                client.connectUser(
                    User(id = response.userId, name = response.userId),
                    token = response.token
                ).enqueue { result ->
                    result.onSuccess { userData ->
                        _uiState = _uiState.copy(
                            isLoggedIn = true,
                            userName = userData.user.id
                        )
                        setState(BaseUiState.Data(_uiState))
                        setEffect(LoginScreenEffect.NavigateToChannelScreen(userName = userData.user.name))
                    }.onError { error ->
                        setState(BaseUiState.Error(Throwable(error.message)))
                    }
                }
            }
            .onError { err ->
                setState(BaseUiState.Error(Throwable(err.name)))
            }
    }
}