package com.bs23.streamchat.core.presentation.base

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow


/**
 * An abstract ViewModel class for implementing the Model-View-Intent (MVI) pattern.
 *
 * This [MVIViewModel] class builds on [BaseViewModel] and provides reactive state management using
 * [BaseUiState], [EVENT] for triggering events, and [EFFECT] for one-time effects.
 *
 * - [uiState]: A state flow representing the current [BaseUiState] of the UI.
 * - [effect]: A channel flow used for emitting one-time effects to the UI.
 *
 * @param STATE Represents the state type which extends [BaseUiState].
 * @param EVENT The type of events that trigger state updates.
 * @param EFFECT The type of effects that represent one-time UI actions.
 *
 *
 * @constructor Initializes the MVIViewModel with [BaseUiState.Empty] as the default UI state.
 *
 * @function onTriggerEvent Abstract method to be implemented for handling each [EVENT].
 * @function setState Sets a new state asynchronously within a coroutine.
 * @function setEffect Emits a one-time effect to the UI within a coroutine.
 *
 *
 * ```kotlin
 * viewModel.setState(MyState.Loading)
 * viewModel.setEffect(MyEffect.ShowToast("Error"))
 * ```
 *
 * @see BaseUiState
 * @see BaseViewModel
 * @property _uiState A [MutableStateFlow] that holds the UI state and allows setting a new state.
 * @property uiState Public [asStateFlow] of [BaseUiState] to observe changes.
 * @property _effect A [Channel] for emitting one-time UI effects, exposed as a flow.
 * @property effect Public [receiveAsFlow] for observing one-time effects.
 *
 * @author Md Asfakur Rahat
 */

abstract class MVIViewModel<STATE: BaseUiState<*>, EVENT, EFFECT> : BaseViewModel() {

    private val _uiState = MutableStateFlow<BaseUiState<*>>(BaseUiState.Empty)
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<EFFECT>()
    val effect = _effect.receiveAsFlow()


    abstract fun onTriggerEvent(eventType: EVENT)
    abstract fun initUiState()

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