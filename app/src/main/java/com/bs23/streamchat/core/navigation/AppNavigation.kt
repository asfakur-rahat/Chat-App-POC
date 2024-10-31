package com.bs23.streamchat.core.navigation

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.bs23.streamchat.core.presentation.components.BaseScreen
import com.bs23.streamchat.message_app.presentation.channels.ChannelListScreenContent
import com.bs23.streamchat.message_app.presentation.channels.ChannelListScreenEffect
import com.bs23.streamchat.message_app.presentation.channels.ChannelListScreenEvent
import com.bs23.streamchat.message_app.presentation.channels.ChannelViewModel
import com.bs23.streamchat.message_app.presentation.login.LoginScreenContent
import com.bs23.streamchat.message_app.presentation.login.LoginScreenEffect
import com.bs23.streamchat.message_app.presentation.login.LoginViewModel
import com.bs23.streamchat.message_app.presentation.message.ChannelMessageContent
import com.bs23.streamchat.message_app.presentation.message.ChannelMessageScreenEvent
import com.bs23.streamchat.message_app.presentation.message.ChannelMessageViewModel
import com.bs23.streamchat.message_app.presentation.signup.SignUpScreenContent
import com.bs23.streamchat.message_app.presentation.signup.SignupScreenEffect
import com.bs23.streamchat.message_app.presentation.signup.SignupViewModel
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Serializable
data object Login

@Serializable
data object SignUp

@Serializable
data class Channel(val userId: String)

@Serializable
data class Message(val channelId: String, val channelName: String)


@Composable
fun AppNavigation(
    navController: NavHostController,
) {
    NavHost(startDestination = Login, navController = navController) {
        composable<SignUp> {
            val viewModel: SignupViewModel = koinViewModel()
            viewModel.initUiState()
            BaseScreen(
                viewModel = viewModel,
                onRetry = {
                    viewModel.initUiState()
                },
                handleEffects = { effect ->
                    when(effect){
                        is SignupScreenEffect.NavigateToChannelScreen -> {
                            navController.navigate(Channel(effect.userID)) {
                                popUpTo<SignUp> {
                                    inclusive = true
                                }
                            }
                        }
                    }
                }
            ){  uiState ->
                SignUpScreenContent(
                    uiState = uiState,
                    onEvent = viewModel::onTriggerEvent,
                    gotoLogIn = {
                        navController.navigate(Login) {
                            popUpTo<SignUp> {
                                inclusive = true
                            }
                        }
                    }
                )
            }
        }
        composable<Login> {
            val viewModel: LoginViewModel = koinViewModel()
            viewModel.initUiState()
            BaseScreen(
                viewModel = viewModel,
                onRetry = {
                    viewModel.initUiState()
                },
                handleEffects = { effect ->
                    when (effect) {
                        is LoginScreenEffect.NavigateToChannelScreen -> {
                            navController.navigate(Channel(effect.userName)) {
                                popUpTo<Login> {
                                    inclusive = true
                                }
                            }
                        }
                    }
                }
            ) { uiState ->
                LoginScreenContent(
                    uiState = uiState,
                    onEvent = viewModel::onTriggerEvent,
                    gotoSignUp = {
                        navController.navigate(SignUp){
                            popUpTo<Login> {
                                inclusive = true
                            }
                        }
                    }
                )
            }
        }
        composable<Channel> {
            val channel = it.toRoute<Channel>()
            val activity = LocalContext.current as Activity
            val viewModel: ChannelViewModel = koinViewModel()
            BaseScreen(
                viewModel = viewModel,
                onRetry = {
                    viewModel.initUiState()
                },
                oneTimeTrigger = {
                    viewModel.onTriggerEvent(ChannelListScreenEvent.SetCurrentUser(channel.userId))
                },
                handleEffects = { effect ->
                    when (effect) {
                        is ChannelListScreenEffect.NavigateToChannel -> {
                            navController.navigate(
                                Message(
                                    channelId = effect.channelId,
                                    channelName = effect.channelName
                                )
                            )
                        }
                        ChannelListScreenEffect.OnLogOut -> {
                            navController.navigate(Login) {
                                popUpTo<Channel> {
                                    inclusive = true
                                }
                            }
                        }
                    }
                }
            ) { uiState ->
                ChannelListScreenContent(
                    uiState = uiState,
                    onEvent = viewModel::onTriggerEvent,
                    backPress = {
                        activity.finish()
                    },
                    userId = channel.userId
                )
            }
        }
        composable<Message> {
            val message = it.toRoute<Message>()
            val viewModel: ChannelMessageViewModel = koinViewModel()
            BaseScreen(
                viewModel = viewModel,
                onRetry = {
                    viewModel.initUiState()
                },
                oneTimeTrigger = {
                    viewModel.onTriggerEvent(ChannelMessageScreenEvent.SetChannelId(channelId = message.channelId))
                }
            ) { uiState ->
                ChannelMessageContent(
                    uiState = uiState,
                    channelId = message.channelId,
                    channelName = message.channelName,
                    onEvent = viewModel::onTriggerEvent,
                    goBack = navController::popBackStack
                )
            }
        }
    }
}