package com.bs23.streamchat.message_app.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TokenDto(
    val access: String,
    val refresh: String,
    @SerialName("stream_sdk_token") val token: String
)
