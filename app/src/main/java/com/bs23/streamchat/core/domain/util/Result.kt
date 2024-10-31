package com.bs23.streamchat.core.domain.util

typealias DomainError = Error


/**
 * A sealed interface representing the outcome of an operation, encapsulating both success and error states.
 *
 * This interface is designed to provide a consistent way to handle the result of operations,
 * such as API calls or computations, where the operation can either succeed with data or fail with an error.
 *
 * It has two implementations:
 *
 * - [Success]: Represents a successful operation and contains the resulting data of type [D].
 * - [Error]: Represents a failed operation and contains the error of type [E], which must be a subtype of [Error].
 *
 * @param D The type of data returned in a successful result.
 * @param E The type of error returned in a failed result, which must implement the [Error] interface.
 *
 * @author Md Asfakur Rahat
 */
sealed interface Result<out D, out E: Error> {
    data class Success<out D>(val data: D): Result<D, Nothing>
    data class Error<out E: DomainError>(val error: E): Result<Nothing, E>
}


/**
 * Transforms the successful data of a [Result] into a new type using a mapping function.
 *
 * This inline function takes a [Result] instance, and if it is a [Success], it applies the provided
 * mapping function to the data, returning a new [Result] containing the transformed data.
 * If the result is an [Error], it simply returns the original error without modification.
 *
 * @param T The type of data in the original [Result].
 * @param E The type of error in the original [Result], which must implement the [Error] interface.
 * @param R The type of data in the resulting [Result] after mapping.
 * @param map A lambda function that transforms the original data of type [T] into a new type [R].
 * @return A new [Result] instance containing the mapped data or the original error.
 *
 * @author Md Asfakur Rahat
 */
inline fun <T, E: Error, R> Result<T, E>.map(map: (T) -> R): Result<R, E> {
    return when(this) {
        is Result.Error -> Result.Error(error)
        is Result.Success -> Result.Success(map(data))
    }
}

/**
 * Transforms the current [Result] into an [EmptyResult], discarding the original data.
 *
 * This extension function on the [Result] type converts both successful and error states
 * into an [EmptyResult]. If the result is a [Success], it ignores the contained data and
 * simply returns an [EmptyResult] with the corresponding error type.
 * If the result is an [Error], it also returns an [EmptyResult] with the original error type.
 *
 * @param T The type of data in the original [Result].
 * @param E The type of error in the original [Result], which must implement the [Error] interface.
 * @return An instance of [EmptyResult] with the same error type as the original [Result].
 *
 * @author Md Asfakur Rahat
 */
fun <T, E: Error> Result<T, E>.asEmptyDataResult(): EmptyResult<E> {
    return map {  }
}

/**
 * Executes a given action if the [Result] is successful, passing the contained data to the action.
 *
 * This inline function checks the current state of the [Result]. If it is a [Success], the provided
 * action is executed with the contained data, and the original [Result] is returned.
 * If the result is an [Error], the original error result is returned without modification.
 *
 * @param T The type of data in the [Result].
 * @param E The type of error in the [Result], which must implement the [Error] interface.
 * @param action A lambda function that is executed when the result is successful, receiving the data of type [T].
 * @return The original [Result] instance, either [Success] or [Error].
 *
 * @author Md Asfakur Rahat
 */
inline fun <T, E: Error> Result<T, E>.onSuccess(action: (T) -> Unit): Result<T, E> {
    return when(this) {
        is Result.Error -> this
        is Result.Success -> {
            action(data)
            this
        }
    }
}
/**
 * Executes a given action if the [Result] is an error, passing the contained error to the action.
 *
 * This inline function checks the current state of the [Result]. If it is an [Error], the provided
 * action is executed with the contained error, and the original [Result] is returned.
 * If the result is a [Success], the original result is returned without modification.
 *
 * @param T The type of data in the [Result].
 * @param E The type of error in the [Result], which must implement the [Error] interface.
 * @param action A lambda function that is executed when the result is an error, receiving the error of type [E].
 * @return The original [Result] instance, either [Success] or [Error].
 *
 * @author Md Asfakur Rahat
 */
inline fun <T, E: Error> Result<T, E>.onError(action: (E) -> Unit): Result<T, E> {
    return when(this) {
        is Result.Error -> {
            action(error)
            this
        }
        is Result.Success -> this
    }
}

typealias EmptyResult<E> = Result<Unit, E>