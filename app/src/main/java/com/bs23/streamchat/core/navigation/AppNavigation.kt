package com.bs23.streamchat.core.navigation

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.bs23.streamchat.core.presentation.components.ObserveAsEvents
import com.bs23.streamchat.message_app.presentation.channels.ChannelListScreen
import com.bs23.streamchat.message_app.presentation.channels.ChannelListScreenEffect
import com.bs23.streamchat.message_app.presentation.channels.ChannelViewModel
import com.bs23.streamchat.message_app.presentation.login.LoginScreen
import com.bs23.streamchat.message_app.presentation.login.LoginScreenEffect
import com.bs23.streamchat.message_app.presentation.login.LoginViewModel
import com.bs23.streamchat.message_app.presentation.message.ChannelMessageScreen
import com.bs23.streamchat.message_app.presentation.signup.SignUpScreen
import com.bs23.streamchat.message_app.presentation.signup.SignupScreenEffect
import com.bs23.streamchat.message_app.presentation.signup.SignupViewModel
//import kotlinx.coroutines.channels.Channel
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
    NavHost(startDestination = SignUp, navController = navController) {
        composable<SignUp> {
            val viewModel: SignupViewModel = koinViewModel()
            viewModel.initUiState()
            ObserveAsEvents(viewModel.effect) { effect ->
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
            SignUpScreen(viewModel,
                gotoLogIn = {
                    navController.navigate(Login){
                        popUpTo<SignUp> {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable<Login> {
            val viewModel: LoginViewModel = koinViewModel()
            viewModel.initUiState()
            ObserveAsEvents(viewModel.effect) { event ->
                when(event){
                    is LoginScreenEffect.NavigateToChannelScreen -> {
                        navController.navigate(Channel(event.userName)) {
                            popUpTo<Login> {
                                inclusive = true
                            }
                        }
                    }
                }
            }
            LoginScreen(viewModel)
        }
        composable<Channel> {
            val channel = it.toRoute<Channel>()
            val activity = LocalContext.current as Activity
            val viewModel: ChannelViewModel = koinViewModel()
            ObserveAsEvents(events = viewModel.effect) { effect ->
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
                        navController.navigate(SignUp) {
                            popUpTo<Channel> {
                                inclusive = true
                            }
                        }
                    }
                }
            }
            ChannelListScreen(
                userId = channel.userId,
                backPress = {
                    activity.finish()
                }
            )
        }
        composable<Message> {
            val message = it.toRoute<Message>()
            ChannelMessageScreen(channelId = message.channelId, channelName = message.channelName) {
                navController.popBackStack()
            }
        }
    }
}