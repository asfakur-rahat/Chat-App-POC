package com.bs23.streamchat.message_app.presentation.message

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import io.getstream.chat.android.compose.ui.messages.MessagesScreen
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import io.getstream.chat.android.compose.ui.theme.StreamColors
import io.getstream.chat.android.compose.ui.theme.StreamShapes
import io.getstream.chat.android.compose.viewmodel.messages.MessagesViewModelFactory

@Suppress("UNUSED_PARAMETER")
@Composable
fun ChannelMessageContent(
    uiState: ChannelMessageUiState,
    channelId: String,
    channelName: String,
    goBack: () -> Unit,
    onEvent: (ChannelMessageScreenEvent) -> Unit,
) {
    val context = LocalContext.current
    ChatTheme(
        shapes = StreamShapes.defaultShapes().copy(
            avatar = RoundedCornerShape(25),
            header = RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp),
            bottomSheet = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp),
            inputField = RoundedCornerShape(10.dp),
        ),
        colors = if(isSystemInDarkTheme()) StreamColors. defaultDarkColors().copy(
            disabled = MaterialTheme.colorScheme.inversePrimary,
            textHighEmphasis = MaterialTheme.colorScheme.onBackground,
            appBackground = MaterialTheme.colorScheme.background,
            primaryAccent = MaterialTheme.colorScheme.primary.copy(alpha = .8f),
            barsBackground = MaterialTheme.colorScheme.secondaryContainer,
            ownMessagesBackground = MaterialTheme.colorScheme.primaryContainer.copy(alpha = .7f),
            otherMessagesBackground = MaterialTheme.colorScheme.tertiaryContainer,
            ownMessageText = MaterialTheme.colorScheme.onPrimaryContainer,
            otherMessageText = MaterialTheme.colorScheme.onTertiaryContainer
        ) else StreamColors. defaultColors().copy(
            disabled = MaterialTheme.colorScheme.inversePrimary,
            textHighEmphasis = MaterialTheme.colorScheme.onBackground,
            primaryAccent = MaterialTheme.colorScheme.primary.copy(alpha = .8f),
            barsBackground = MaterialTheme.colorScheme.secondaryContainer,
            appBackground = MaterialTheme.colorScheme.background,
            ownMessagesBackground = MaterialTheme.colorScheme.primaryContainer.copy(alpha = .7f),
            otherMessagesBackground = MaterialTheme.colorScheme.tertiaryContainer,
            ownMessageText = MaterialTheme.colorScheme.onPrimaryContainer,
            otherMessageText = MaterialTheme.colorScheme.onTertiaryContainer
        )
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = padding.calculateBottomPadding() - padding.calculateBottomPadding()),
                contentAlignment = Alignment.Center
            ) {
                MessagesScreen(
                    viewModelFactory = MessagesViewModelFactory(
                        context = context,
                        channelId = channelId,
                    ),
                    onBackPressed = {
                        goBack.invoke()
                    }
                )
            }
        }
    }
}