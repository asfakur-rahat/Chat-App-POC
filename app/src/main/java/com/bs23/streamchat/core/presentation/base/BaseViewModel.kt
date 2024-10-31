package com.bs23.streamchat.core.presentation.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * An abstract ViewModel class providing base functionality for handling common UI events and background tasks.
 *
 * This [BaseViewModel] class encapsulates shared functionality that can be extended by other ViewModels.
 * It includes:
 *
 * - A [showToast] method that emits messages to display as toasts.
 * - A [safeLaunch] function for executing coroutines with error handling.
 * - [fetchMultipleResource] to handle multiple asynchronous API calls and invoke a completion handler upon completion.
 * - [safeLaunchIO] for launching coroutines on the IO dispatcher.
 * - [call] for observing a flow and handling errors, optionally invoking a completion handler.
 *
 * @property _showToast A [MutableSharedFlow] for broadcasting toast messages to be observed.
 * @property showToast A [Flow] that external classes observe to receive toast messages.
 * @property handler A [CoroutineExceptionHandler] that catches exceptions and calls [handleError].
 *
 * @constructor Base constructor initializes the [CoroutineExceptionHandler] for error handling.
 *
 * @function handleError Override to provide specific error handling logic.
 * @function startLoading Override to define loading logic, called before executing tasks.
 *
 * @param T The type of data to be collected by the flow in [call].
 * @author Md Asfakur Rahat
 */
abstract class BaseViewModel : ViewModel() {

    private val _showToast = MutableSharedFlow<String>()
    val showToast: Flow<String> get() = _showToast.asSharedFlow()

    fun showToast(message: String) {
        viewModelScope.launch {
            _showToast.emit(message)
        }
    }

    private val handler = CoroutineExceptionHandler { _, exception ->
        handleError(exception)
    }

    open fun handleError(exception: Throwable) {

    }

    open fun startLoading() {

    }

    protected fun safeLaunch(
        disableStartLoading: Boolean = false,
        block: suspend CoroutineScope.() -> Unit,
    ) {
        if (!disableStartLoading) startLoading()
        viewModelScope.launch(context = handler, block = block)
    }

    protected fun <T> fetchMultipleResource(
        vararg apiCalls: suspend () -> T,
        completionHandler: (List<T>) -> Unit = {}
    ){
        startLoading()
        safeLaunchIO{
            val results = apiCalls.map {
                async { it.invoke() }
            }.map { it.await() }
            completionHandler.invoke(results)
        }
    }

    protected fun safeLaunchIO(
        completionHandler: () -> Unit = {},
        block: suspend CoroutineScope.() -> Unit,
    ) {
//        startLoading()
        viewModelScope.launch(context = handler, block = {
            withContext(Dispatchers.IO) {
                block.invoke(this)
            }
        }).invokeOnCompletion {
            completionHandler.invoke()
        }
    }

    protected suspend fun <T> call(
        callFlow: Flow<T>,
        completionHandler: (collect: T) -> Unit = {}
    ) {
        callFlow
            .catch { handleError(it) }
            .collect {
                completionHandler.invoke(it)
            }
    }
}