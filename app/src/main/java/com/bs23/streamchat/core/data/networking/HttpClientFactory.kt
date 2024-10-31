package com.bs23.streamchat.core.data.networking

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json


/**
 * Factory object for creating an instance of [HttpClient] with specified configurations.
 *
 * This object provides a method to create an [HttpClient] using a given [engine].
 * It installs the [Logging] feature to log all requests and responses with the Android logger,
 * and sets up [ContentNegotiation] to handle JSON content with specific settings.
 * The configuration also sets the default content type for requests to `application/json`.
 *
 * @param engine The [HttpClientEngine] to be used for creating the [HttpClient].
 * @return An instance of [HttpClient] configured with logging and content negotiation features.
 *
 * @sample sampleFactory
 *
 * @author Md Asfakur Rahat
 */

object HttpClientFactory {

    fun create(engine: HttpClientEngine): HttpClient {
        return HttpClient(engine) {
            install(Logging) {
                level = LogLevel.ALL
                logger = Logger.ANDROID
            }
            install(ContentNegotiation) {
                json(
                    json = Json {
                        ignoreUnknownKeys = true
                    }
                )
            }
            defaultRequest {
                contentType(ContentType.Application.Json)
            }
        }
    }
}

val sampleFactory = HttpClientFactory.create(CIO.create())