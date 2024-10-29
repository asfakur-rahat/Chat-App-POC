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