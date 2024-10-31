package com.bs23.streamchat.message_app.domain.models

data class LoggedInUser(
    val token: String,
    val userId: String,
)
