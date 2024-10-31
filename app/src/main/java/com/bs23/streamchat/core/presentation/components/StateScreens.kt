package com.bs23.streamchat.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * A composable function displaying a loading screen with a centered [CircularProgressIndicator].
 *
 * This [LoadingScreen] function provides a full-screen loading overlay, using [MaterialTheme.colorScheme]
 * for background and progress indicator colors.
 *```kotlin
 *   LoadingScreen(modifier = Modifier)
 *```
 *
 * @param modifier A [Modifier] for adjusting the layout, passed externally to allow customization.
 *
 * @see Box
 * @see CircularProgressIndicator
 * @see MaterialTheme
 *
 * @author Md Asfakur Rahat
 */
@Composable
fun LoadingScreen(modifier: Modifier) {
    Box(
        modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.onBackground)
    }
}

/**
 * A composable function that displays an error message centered on the screen.
 *
 * This [ErrorScreen] function provides a full-screen overlay with a semi-transparent
 * error background, showing the provided error message in the center.
 *
 * ```kotlin
 *   ErrorScreen(modifier = Modifier, error = Throwable("Network Error"))
 *   ```
 *
 * @param modifier A [Modifier] to adjust layout, enabling external customization.
 * @param error The [Throwable] whose message is displayed on the screen.
 *
 * @see Box
 * @see Text
 * @see MaterialTheme
 * @author Md Asfakur Rahat
 */

@Composable
fun ErrorScreen(modifier: Modifier, error: Throwable, retry: () -> Unit = {}) {
    Box(
        modifier = modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.errorContainer.copy(alpha = .5f)),
        contentAlignment = Alignment.Center
    ) {
        println(error.message)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 24.dp),
                text = error.message ?: "Unknown Error",
                color = MaterialTheme.colorScheme.onErrorContainer
            )
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = retry,
                shape = RoundedCornerShape(25)
            ) {
                Text(text = "   Try Again   ")
            }
        }
    }
}

@Composable
fun EmptyScreen(modifier: Modifier) {
    Box(
        modifier = modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {

    }
}

@Composable
fun ContentScreen(
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = topBar,
        bottomBar = bottomBar
    ){
        content(it)
    }
}