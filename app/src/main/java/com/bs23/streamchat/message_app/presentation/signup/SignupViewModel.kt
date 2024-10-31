package com.bs23.streamchat.message_app.presentation.signup

import com.bs23.streamchat.core.domain.util.onError
import com.bs23.streamchat.core.domain.util.onSuccess
import com.bs23.streamchat.core.presentation.base.BaseUiState
import com.bs23.streamchat.core.presentation.base.MVIViewModel
import com.bs23.streamchat.message_app.domain.ChatAppDataSource
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.models.User

// Username can only contain a-z,A-Z,0-9,_ and @ At Least 3 characters
private val USER_REGEX = Regex("^[a-zA-Z0-9_@]{3,}\$")


class SignupViewModel(
    private val dataSource: ChatAppDataSource,
    private val client: ChatClient,
) : MVIViewModel<BaseUiState<SignupScreenUiState>, SignupScreenEvent, SignupScreenEffect>() {

    private var _uiState = SignupScreenUiState()

    override fun initUiState() {
        _uiState = SignupScreenUiState()
        setState(BaseUiState.Data(_uiState))
    }

    override fun onTriggerEvent(eventType: SignupScreenEvent) {
        when (eventType) {
            is SignupScreenEvent.UserSignUp -> userSignUp(
                userName = eventType.userName,
                email = eventType.email,
                password = eventType.password,
                confirmPassword = eventType.confirmPassword
            )

            is SignupScreenEvent.OnConfirmPasswordChanged -> {
                _uiState = _uiState.copy(
                    confirmPassword = eventType.confirmPassword
                )
                setState(BaseUiState.Data(_uiState))
            }

            is SignupScreenEvent.OnEmailChanged -> {
                _uiState = _uiState.copy(
                    email = eventType.email
                )
                setState(BaseUiState.Data(_uiState))
            }

            is SignupScreenEvent.OnPasswordChanged -> {
                _uiState = _uiState.copy(
                    password = eventType.password
                )
                setState(BaseUiState.Data(_uiState))
            }

            is SignupScreenEvent.UserNameChanged -> {
                _uiState = _uiState.copy(
                    userName = eventType.userName
                )
                setState(BaseUiState.Data(_uiState))
            }
        }
    }


    private fun validatePassword(p1: String, p2: String) = p1 == p2

    private fun validateUserName(userName: String) = userName.matches(USER_REGEX)

    private fun userSignUp(
        userName: String,
        email: String,
        password: String,
        confirmPassword: String,
    ) {
        if (validatePassword(password, confirmPassword)) {
            if (validateUserName(userName)) {
                safeLaunch {
                    dataSource.signUp(userName, email, password, confirmPassword)
                        .onSuccess {
                            dataSource.signInAndGetToken(email, password)
                                .onSuccess {
                                    client.connectUser(
                                        user = User(
                                            id = userName,
                                            name = userName,
                                            role = "user"
                                        ),
                                        token = it.token
                                    ).enqueue { result ->
                                        //println("Result after connecting user $result")
                                        result.onSuccess { data ->
                                            _uiState = _uiState.copy(
                                                userID = data.user.id,
                                            )
                                            setState(BaseUiState.Data(_uiState))
                                            setEffect(SignupScreenEffect.NavigateToChannelScreen(_uiState.userID))
                                        }.onError { error ->
                                            setState(
                                                BaseUiState.Error(
                                                    Throwable(error.message)
                                                )
                                            )
                                        }
                                    }
                                }
                                .onError {
                                    setState(BaseUiState.Error(Throwable(it.name)))
                                }
                        }
                        .onError {
                            setState(BaseUiState.Error(Throwable(it.name)))
                        }
                }
            } else {
                _uiState = _uiState.copy(
                    userNameError = "Username is not valid"
                )
                setState(BaseUiState.Data(_uiState))
            }
        } else {
            _uiState = _uiState.copy(
                confirmPasswordError = "Passwords do not match"
            )
            setState(BaseUiState.Data(_uiState))
        }
    }
}