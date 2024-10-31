package com.bs23.streamchat.core.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bs23.streamchat.core.presentation.base.BaseUiState
import com.bs23.streamchat.core.presentation.base.MVIViewModel
import com.bs23.streamchat.core.presentation.util.cast
import kotlinx.coroutines.CoroutineScope

/**
 * A composable function that renders a screen based on [BaseUiState] and observes the view model effects.
 *
 * This [BaseScreen] function handles various UI states (loading, empty, data, and error) and allows handling of
 * one-time triggers and side effects. The [content] composable displays the main content when the state is
 * [BaseUiState.Data].
 *
 * @param viewModel The [MVIViewModel] managing the UI state, events, and effects.
 * @param handleEffects A lambda to handle side effects emitted from the view model.
 * @param oneTimeTrigger A suspending lambda that runs a one-time action when the composable is first launched.
 * @param content A composable function that renders the main content for the data state.
 *
 * ```kotlin
 * BaseScreen(
 *      viewModel = myViewModel,
 *      oneTimeTrigger = {
 *          // Call any event you want to trigger only once
 *      }
 *      handleEffects = { effect ->
 *          // Handle effect
 *      }
 * ) { state ->
 *     // Render screen content because this is always data state
 * }
 * ```
 *
 * @see BaseUiState
 * @see ObserveAsEvents
 * @see LaunchedEffect
 *
 * @author Md Asfakur Rahat
 */

@Composable
fun <STATE, EVENT, EFFECT> BaseScreen(
    viewModel: MVIViewModel<BaseUiState<STATE>, EVENT, EFFECT>,
    handleEffects: (effect: EFFECT) -> Unit = {},
    oneTimeTrigger: suspend CoroutineScope.() -> Unit = {},
    content: @Composable (uiState: STATE) -> Unit
) {
    val baseState by viewModel.uiState.collectAsStateWithLifecycle()
    ObserveAsEvents(viewModel.effect) { effect ->
        handleEffects(effect)
    }
    LaunchedEffect(Unit, block = oneTimeTrigger)
    when(baseState){
        is BaseUiState.Data -> {
            val data = baseState.cast<BaseUiState.Data<STATE>>().data
            content(data)
        }
        BaseUiState.Empty -> {
            EmptyScreen(Modifier)
        }
        is BaseUiState.Error -> {
            val error = baseState.cast<BaseUiState.Error>().error
            ErrorScreen(Modifier, error)
        }
        BaseUiState.Loading -> {
            LoadingScreen(Modifier)
        }
    }
}