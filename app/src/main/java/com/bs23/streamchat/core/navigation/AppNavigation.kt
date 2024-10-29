package com.bs23.streamchat.core.navigation

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.bs23.streamchat.message_app.presentation.channels.ChannelListScreen
import com.bs23.streamchat.message_app.presentation.login.LoginScreen
import com.bs23.streamchat.message_app.presentation.login.LoginViewModel
import com.bs23.streamchat.message_app.presentation.message.ChannelMessageScreen
import com.bs23.streamchat.message_app.presentation.signup.SignUpScreen
import com.bs23.streamchat.message_app.presentation.signup.SignupViewModel
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Serializable
data object Login

@Serializable
data object SignUp

@Serializable
data class  Channel(val userId: String)

@Serializable
data class  Message(val channelId: String, val channelName: String)


@Composable
fun AppNavigation(
    navController: NavHostController,
){
    NavHost(startDestination = SignUp, navController = navController){
        composable<SignUp> {
            val viewModel: SignupViewModel = koinViewModel()
            viewModel.initUiState()
            SignUpScreen(viewModel) { userID ->
                navController.navigate(Channel(userID)) {
                    popUpTo<SignUp> {
                        inclusive = true
                    }
                }
            }
        }
        composable<Login> {
            val viewModel: LoginViewModel = koinViewModel()
            viewModel.initUiState()
            LoginScreen(viewModel) {
                navController.navigate(Channel(it))
            }
        }
        composable<Channel> {
            val channel = it.toRoute<Channel>()
            val activity = LocalContext.current as Activity
            ChannelListScreen(
                userId = channel.userId,
                onChannelClick = { channelId, channelName ->
                    navController.navigate(Message(channelId = channelId, channelName = channelName))
                },
                onDismiss = {
                    activity.finish()
                },
                onLogout = {
                    navController.navigate(SignUp){
                        popUpTo<Channel>{
                            inclusive = true
                        }
                    }
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