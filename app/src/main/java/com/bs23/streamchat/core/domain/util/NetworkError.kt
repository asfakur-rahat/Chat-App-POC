package com.bs23.streamchat.core.domain.util

/**
 * Enum class representing various types of network errors that can occur during API requests.
 *
 * This enum implements the [Error] interface, providing a structured way to categorize
 * network-related issues that may arise during communication with a server.
 * Each constant represents a specific error scenario:
 *
 * - [REQUEST_TIMEOUT]: Indicates that the request to the server timed out.
 * - [TOO_MANY_REQUESTS]: Indicates that the client has sent too many requests in a given timeframe.
 * - [NO_INTERNET]: Indicates that there is no internet connectivity.
 * - [SERVER_ERROR]: Indicates an error occurred on the server side.
 * - [SERIALIZATION]: Indicates an error related to serialization or deserialization of data.
 * - [UNKNOWN]: Represents any other unknown network error.
 *
 * @author Md Asfakur Rahat
 */

enum class NetworkError: Error {
    REQUEST_TIMEOUT,
    TOO_MANY_REQUESTS,
    NO_INTERNET,
    SERVER_ERROR,
    SERIALIZATION,
    UNKNOWN,
}