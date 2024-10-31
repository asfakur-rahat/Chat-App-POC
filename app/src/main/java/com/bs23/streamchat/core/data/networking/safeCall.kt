package com.bs23.streamchat.core.data.networking

import com.bs23.streamchat.core.domain.util.NetworkError
import com.bs23.streamchat.core.domain.util.Result
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.ensureActive
import kotlinx.serialization.SerializationException
import kotlin.coroutines.coroutineContext


/**
 * Executes a safe call to obtain an [HttpResponse] and converts it to a [Result].
 *
 * This suspend function attempts to execute the provided `execute` function, which is expected to
 * return an [HttpResponse]. If any exceptions occur during the execution, they are caught and
 * mapped to corresponding [NetworkError] values:
 *
 * - [UnresolvedAddressException]: Indicates no internet connection, returning [NetworkError.NO_INTERNET].
 * - [SerializationException]: Indicates an error occurred during serialization, returning [NetworkError.SERIALIZATION].
 * - Any other exception: Returns [NetworkError.UNKNOWN] after ensuring the coroutine context is active.
 *
 * If the call is successful, the response is processed through the [responseToResult] function to
 * produce a final [Result].
 *
 * @param T The type of the expected data returned in the successful result.
 * @param execute A lambda function that executes the network call and returns an [HttpResponse].
 * @return A [Result] instance that encapsulates either the deserialized data or an error.
 *
 * @author Md Asfakur Rahat
 */
suspend inline fun <reified T> safeCall(
    execute: () -> HttpResponse
): Result<T, NetworkError> {
    val response = try {
        execute()
    } catch(e: UnresolvedAddressException) {
        return Result.Error(NetworkError.NO_INTERNET)
    } catch(e: SerializationException) {
        return Result.Error(NetworkError.SERIALIZATION)
    } catch(e: Exception) {
        coroutineContext.ensureActive()
        return Result.Error(NetworkError.UNKNOWN)
    }

    return responseToResult(response)
}