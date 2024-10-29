package com.bs23.streamchat.message_app.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class TokenRequest(
    val email: String,
    val password: String
)
