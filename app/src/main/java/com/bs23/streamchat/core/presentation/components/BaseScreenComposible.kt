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