package com.bs23.streamchat.message_app.data.dto

import com.bs23.streamchat.message_app.domain.models.UserSignUp
import kotlinx.serialization.Serializable

@Serializable
data class SignUpResponseDto(
    val id: Int,
    val email: String,
    val message: String
)

fun SignUpResponseDto.toUserSignUp() = UserSignUp(
    id = id,
    email = email,
    message = message
)
