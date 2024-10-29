package com.bs23.streamchat.message_app.presentation.message

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bs23.streamchat.core.presentation.base.BaseUiState
import com.bs23.streamchat.core.presentation.components.EmptyScreen
import com.bs23.streamchat.core.presentation.components.ErrorScreen
import com.bs23.streamchat.core.presentation.components.LoadingScreen
import com.bs23.streamchat.core.presentation.util.cast
import io.getstream.chat.android.compose.ui.messages.MessagesScreen
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import io.getstream.chat.android.compose.ui.theme.StreamColors
import io.getstream.chat.android.compose.ui.theme.StreamShapes
import io.getstream.chat.android.compose.viewmodel.messages.MessagesViewModelFactory
import org.koin.androidx.compose.koinViewModel

@Composable
fun ChannelMessageScreen(
    channelId: String,
    channelName: String,
    goBack: () -> Unit,
) {
    val viewModel: ChannelMessageViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onTriggerEvent(ChannelMessageScreenEvent.SetChannelId(channelId))
    }

    when (uiState) {
        is BaseUiState.Data -> {
            val data = uiState.cast<BaseUiState.Data<ChannelMessageUiState>>().data
            ChannelMessageContent(
                uiState = data,
                channelId = channelId,
                channelName = channelName,
                onEvent = viewModel::onTriggerEvent,
                goBack = goBack
            )

        }

        BaseUiState.Empty -> {
            EmptyScreen(Modifier)
        }

        is BaseUiState.Error -> {
            val error = uiState.cast<BaseUiState.Error>().error
            ErrorScreen(Modifier, error)
        }

        BaseUiState.Loading -> {
            LoadingScreen(Modifier)
        }
    }
}

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
                    },
                    topBarContent = {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .clip(ChatTheme.shapes.header)
                                .background(ChatTheme.colors.barsBackground)
                                .padding(vertical = 5.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            IconButton(
                                modifier = Modifier
                                    .align(Alignment.CenterStart),
                                onClick = {
                                    it.invoke()
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                    contentDescription = "BACK"
                                )
                            }
                            Text(text = channelName)
                        }
                    }
                )
            }
        }
    }
}