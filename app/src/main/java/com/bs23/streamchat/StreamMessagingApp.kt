package com.bs23.streamchat

import android.app.Application
import com.bs23.streamchat.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class StreamMessagingApp: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@StreamMessagingApp)
            modules(appModule)
        }
    }
}