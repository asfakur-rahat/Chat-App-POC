package com.bs23.streamchat.message_app.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignUpRequest(
    @SerialName("user_id_streamsdk") val userID: String,
    val email: String,
    val password: String,
    @SerialName("confirm_password")val confirmPassword: String
)
