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
import com.bs23.streamchat.message_app.presentation.message.ChannelMessageScreen
import kotlinx.serialization.Serializable

@Serializable
data object Login

@Serializable
data class  Channel(val userId: String)

@Serializable
data class  Message(val channelId: String, val channelName: String)


@Composable
fun AppNavigation(
    navController: NavHostController,
){
    NavHost(startDestination = Login, navController = navController){
        composable<Login> {
            LoginScreen {
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