package com.bs23.streamchat.core.presentation.util



/**
 * Safely casts an instance to the specified type [T].
 *
 * This [cast] function uses Kotlin's `reified` type parameter to attempt a cast of `Any` to [T].
 * It should be used cautiously, as incorrect casts will throw a [ClassCastException].
 * ```kotlin
 *    val data = baseState.cast<BaseUiState.Data<STATE>>()
 *  ```
 * @param T The target type to cast the instance to.
 * @receiver Any instance to be cast.
 * @return The instance cast to type [T].
 *
 * @throws ClassCastException if the cast is not possible.
 * @author Md Asfakur Rahat
 */

inline fun <reified T : Any> Any.cast(): T {
    return this as T
}