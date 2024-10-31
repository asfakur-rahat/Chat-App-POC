package com.bs23.streamchat.core.presentation.components

import androidx.compose.runtime.Composable
import com.bs23.streamchat.core.presentation.base.BaseUiState
import com.bs23.streamchat.core.presentation.base.MVIViewModel

@Composable
fun <STATE : BaseUiState<*>, EVENT, EFFECT> BaseScreen(
    viewModel: MVIViewModel<STATE, EVENT, EFFECT>,
    handleEffects: (EFFECT) -> Unit,
    content: @Composable () -> Unit
) {
    ObserveAsEvents(viewModel.effect) { effect ->
        handleEffects(effect)
    }
    content()
}