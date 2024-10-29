package com.bs23.streamchat.core.presentation.base

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class MVIViewModel<STATE: BaseUiState<*>, EVENT> : BaseViewModel() {

    private val _uiState = MutableStateFlow<BaseUiState<*>>(BaseUiState.Empty)
    val uiState = _uiState.asStateFlow()

    abstract fun onTriggerEvent(eventType: EVENT)

    protected fun setState(state: STATE) = safeLaunch {
        _uiState.emit(state)
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