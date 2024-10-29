package com.bs23.streamchat.message_app.data

import com.bs23.streamchat.core.data.networking.constructUrl
import com.bs23.streamchat.core.data.networking.safeCall
import com.bs23.streamchat.core.domain.util.NetworkError
import com.bs23.streamchat.core.domain.util.Result
import com.bs23.streamchat.core.domain.util.map
import com.bs23.streamchat.message_app.data.dto.SignUpRequest
import com.bs23.streamchat.message_app.data.dto.SignUpResponseDto
import com.bs23.streamchat.message_app.data.dto.TokenDto
import com.bs23.streamchat.message_app.data.dto.TokenRequest
import com.bs23.streamchat.message_app.data.dto.toUserSignUp
import com.bs23.streamchat.message_app.domain.ChatAppDataSource
import com.bs23.streamchat.message_app.domain.models.UserSignUp
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class RemoteChatAppDataSource(
    private val client: HttpClient
): ChatAppDataSource {
    override suspend fun signUp(
        userID: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Result<UserSignUp, NetworkError> {
        return safeCall<SignUpResponseDto> {
            client.post(
                urlString = constructUrl("/signup/")
            ){
                setBody(
                    SignUpRequest(
                        userID = userID,
                        email = email,
                        password = password,
                        confirmPassword = confirmPassword
                    )
                )
            }
        }.map { result ->
            result.toUserSignUp()
        }
    }

    override suspend fun signInAndGetToken(
        email: String,
        password: String
    ): Result<String, NetworkError> {
        return safeCall<TokenDto> {
            client.post(
                urlString = constructUrl("/token/")
            ){
                setBody(
                    TokenRequest(
                        email = email,
                        password = password
                    )
                )
            }
        }.map {
            it.token
        }
    }
}