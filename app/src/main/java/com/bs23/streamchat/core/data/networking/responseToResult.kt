package com.bs23.streamchat.core.data.networking

import com.bs23.streamchat.core.domain.util.NetworkError
import com.bs23.streamchat.core.domain.util.Result
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse


/**
 * Converts an [HttpResponse] to a [Result] type, mapping HTTP response statuses to success or error states.
 *
 * This suspend function processes the given [HttpResponse] and checks its status code. If the status
 * indicates success (2xx), it attempts to deserialize the response body into the specified type [T].
 * If successful, it returns a [Result.Success] containing the data. If deserialization fails, it returns
 * a [Result.Error] with a serialization error.
 *
 * For specific error codes, it maps them to corresponding [NetworkError] values:
 * - 408: Request Timeout
 * - 429: Too Many Requests
 * - 5xx: Server Error
 *
 * Any other status codes are treated as unknown errors.
 *
 * @param T The type of the expected data returned in the successful result.
 * @param response The [HttpResponse] to be processed.
 * @return A [Result] instance that encapsulates either the deserialized data or an error.
 *
 * @throws NoTransformationFoundException If the response body cannot be transformed into the specified type.
 *
 * @author Md Asfakur Rahat
 */
suspend inline fun <reified T> responseToResult(
    response: HttpResponse
): Result<T, NetworkError> {
    return when(response.status.value) {
        in 200..299 -> {
            try {
                Result.Success(response.body<T>())
            } catch(e: NoTransformationFoundException) {
                Result.Error(NetworkError.SERIALIZATION)
            }
        }
        408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
        429 -> Result.Error(NetworkError.TOO_MANY_REQUESTS)
        in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
        else -> Result.Error(NetworkError.UNKNOWN)
    }
}