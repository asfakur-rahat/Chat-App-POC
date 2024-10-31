package com.bs23.streamchat.core.data.networking

import com.bs23.streamchat.BuildConfig


/**
 * Constructs a complete URL by appending a base URL if not already included.
 *
 * Checks if the given [url] contains the base URL defined in `BuildConfig.BASE_URL`.
 * If present, the URL is returned as-is; otherwise, the base URL is prepended.
 * Removes any leading slash in [url] to avoid duplicate slashes in the final URL.
 *
 * @param url The relative or absolute URL to be completed.
 * @return A complete URL as a [String] that includes the base URL if it was not initially present.
 *
 * @sample sample
 *
 * @author Md Asfakur Rahat
 */

fun constructUrl(url: String): String {
    return when {
        url.contains(BuildConfig.BASE_URL) -> url
        url.startsWith("/") -> BuildConfig.BASE_URL + url.drop(1)
        else -> BuildConfig.BASE_URL + url
    }
}

val sample = constructUrl("/api/v1/resource")