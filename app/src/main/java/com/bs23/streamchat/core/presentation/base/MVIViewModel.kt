package com.bs23.streamchat.core.presentation.base

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

abstract class MVIViewModel<STATE: BaseUiState<*>, EVENT, EFFECT> : BaseViewModel() {

    private val _uiState = MutableStateFlow<BaseUiState<*>>(BaseUiState.Empty)
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<EFFECT>()
    val effect = _effect.receiveAsFlow()


    abstract fun onTriggerEvent(eventType: EVENT)

    protected fun setState(state: STATE) = safeLaunch {
        _uiState.emit(state)
    }

    protected fun setEffect(effect: EFFECT) = safeLaunch {
        _effect.send(effect)
    }

    override fun startLoading() {
        super.startLoading()
        _uiState.value = BaseUiState.Loading
    }

    override fun handleError(exception: Throwable) {
        super.handleError(exception)
        _uiState.value = BaseUiState.Error(exception)
    }
}