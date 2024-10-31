package com.bs23.streamchat.message_app.domain

import com.bs23.streamchat.core.domain.util.NetworkError
import com.bs23.streamchat.core.domain.util.Result
import com.bs23.streamchat.message_app.domain.models.LoggedInUser
import com.bs23.streamchat.message_app.domain.models.UserSignUp


interface ChatAppDataSource {
    suspend fun signUp(userID: String, email: String, password: String, confirmPassword: String): Result<UserSignUp, NetworkError>
    suspend fun signInAndGetToken(email: String, password: String): Result<LoggedInUser, NetworkError>
}