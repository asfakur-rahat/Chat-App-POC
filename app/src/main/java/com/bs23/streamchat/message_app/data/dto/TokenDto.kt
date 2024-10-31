package com.bs23.streamchat.message_app.data.dto

import com.bs23.streamchat.message_app.domain.models.LoggedInUser
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TokenDto(
    val access: String,
    val refresh: String,
    @SerialName("user_id_streamsdk") val userID: String,
    @SerialName("stream_sdk_token") val token: String
)

fun TokenDto.toLoggedInUser(): LoggedInUser {
    return LoggedInUser(
        token = token,
        userId = userID
    )
}
