package com.bs23.streamchat.core.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

/**
 * A composable function that observes a [Flow] of events and handles each event with the provided [onEvent] lambda.
 *
 * This [ObserveAsEvents] function starts collecting the [events] flow when the lifecycle is in the [Lifecycle.State.STARTED] state,
 * ensuring the collection aligns with the composable's lifecycle.
 *```kotlin
 *   ObserveAsEvents(events = viewModel.effect) { event ->
 *       // Handle event
 *   }
 *```
 *
 * @param events The [Flow] of events to observe and act upon.
 * @param key1 Optional key to control recomposition, typically used for state.
 * @param key2 Optional additional key to control recomposition.
 * @param onEvent Lambda that handles each emitted event from [events].
 *
 * @see LaunchedEffect
 * @see repeatOnLifecycle
 * @see LocalLifecycleOwner
 *
 * @author Md Asfakur Rahat
 */

@Composable
fun <T> ObserveAsEvents(
    events: Flow<T>,
    key1: Any? = null,
    key2: Any? = null,
    onEvent: (T) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner.lifecycle, key1, key2) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            withContext(Dispatchers.Main.immediate) {
                events.collect(onEvent)
            }
        }
    }
}