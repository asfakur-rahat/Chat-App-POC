package com.bs23.streamchat.core.presentation.base


/**
 * A sealed interface representing the UI state for data-driven views.
 *
 * This interface defines different states that the UI can be in while processing or displaying data.
 * It includes:
 *
 * - [Loading]: Indicates that data is being loaded.
 * - [Empty]: Represents an empty state where no data is available.
 * - [Data]: Contains the actual data of type [T] when the data is successfully loaded.
 * - [Error]: Represents an error state and contains a [Throwable] to describe the error that occurred.
 *
 * @param T The type of data contained in the [Data] state.
 *
 * @author Md Asfakur Rahat
 */
sealed interface BaseUiState<out T>{
    data object Loading: BaseUiState<Nothing>
    data object Empty: BaseUiState<Nothing>
    data class Data<T>(val data: T): BaseUiState<T>
    data class Error(val error: Throwable): BaseUiState<Nothing>
}