package com.bs23.streamchat.core.presentation.util

inline fun <reified T : Any> Any.cast(): T {
    return this as T
}