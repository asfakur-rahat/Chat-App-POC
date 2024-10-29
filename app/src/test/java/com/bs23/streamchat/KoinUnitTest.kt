package com.bs23.streamchat

import com.bs23.streamchat.di.appModule
import org.junit.Test

import org.junit.Assert.*
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.KoinTest
import org.koin.test.verify.verify

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class KoinUnitTest: KoinTest {
    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun checkAllModules(){
        appModule.verify()
    }
}