package com.bs23.streamchat.di

import android.content.Context
import com.bs23.streamchat.BuildConfig
import com.bs23.streamchat.core.data.networking.HttpClientFactory
import com.bs23.streamchat.message_app.data.RemoteChatAppDataSource
import com.bs23.streamchat.message_app.domain.ChatAppDataSource
import com.bs23.streamchat.message_app.presentation.channels.ChannelViewModel
import com.bs23.streamchat.message_app.presentation.login.LoginViewModel
import com.bs23.streamchat.message_app.presentation.message.ChannelMessageViewModel
import com.bs23.streamchat.message_app.presentation.signup.SignupViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.logger.ChatLogLevel
import io.getstream.chat.android.offline.plugin.factory.StreamOfflinePluginFactory
import io.getstream.chat.android.state.plugin.config.StatePluginConfig
import io.getstream.chat.android.state.plugin.factory.StreamStatePluginFactory
import io.ktor.client.engine.cio.CIO
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun getOfflinePluginFactory(context: Context) = StreamOfflinePluginFactory(appContext = context.applicationContext)
fun getStatePluginFactory(context: Context) = StreamStatePluginFactory(config = StatePluginConfig(), appContext = context)
fun getChatClient(context: Context, offlinePluginFactory: StreamOfflinePluginFactory, statePluginFactory: StreamStatePluginFactory) =
    ChatClient.Builder(BuildConfig.API_KEY, appContext = context.applicationContext)
        .withPlugins(offlinePluginFactory, statePluginFactory)
        .logLevel(ChatLogLevel.ALL)
        .build()

val appModule = module {
    factory {
        getOfflinePluginFactory(get())
    }
    factory {
        getStatePluginFactory(get())
    }
    single { HttpClientFactory.create(CIO.create()) }
    single {
        getChatClient(get(), get(), get())
    }

    singleOf(::RemoteChatAppDataSource).bind<ChatAppDataSource>()
    viewModelOf(::SignupViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::ChannelViewModel)
    viewModelOf(::ChannelMessageViewModel)
}