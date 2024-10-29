package com.bs23.streamchat.message_app.presentation.channels

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bs23.streamchat.core.presentation.base.BaseUiState
import com.bs23.streamchat.core.presentation.components.EmptyScreen
import com.bs23.streamchat.core.presentation.components.ErrorScreen
import com.bs23.streamchat.core.presentation.components.LoadingScreen
import com.bs23.streamchat.core.presentation.util.cast
import io.getstream.chat.android.compose.ui.channels.ChannelsScreen
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import io.getstream.chat.android.compose.ui.theme.StreamColors
import io.getstream.chat.android.compose.ui.theme.StreamShapes
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun ChannelListScreen(
    onChannelClick: (String, String) -> Unit,
    onDismiss: () -> Unit,
    onLogout: () -> Unit,
    userId: String,
) {
    val viewModel: ChannelViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onTriggerEvent(ChannelListScreenEvent.SetCurrentUser(userId))
    }

    when (uiState) {
        is BaseUiState.Data -> {
            val data = uiState.cast<BaseUiState.Data<ChannelListScreenUiState>>().data
            ChannelListScreenContent(
                uiState = data,
                onEvent = viewModel::onTriggerEvent,
                onNavigate = onChannelClick,
                onDismiss = {
                    if (it) {
                        //viewModel.onTriggerEvent(ChannelListScreenEvent.OnClickLogout)
                        onLogout.invoke()
                    } else {
                        onDismiss.invoke()
                    }
                },
                userId = userId
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChannelListScreenContent(
    uiState: ChannelListScreenUiState,
    onEvent: (ChannelListScreenEvent) -> Unit,
    onNavigate: (String, String) -> Unit,
    onDismiss: (Boolean) -> Unit,
    userId: String,
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    LaunchedEffect(uiState.isLoggedIn) {
        if (!uiState.isLoggedIn) {
            onDismiss.invoke(true)
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(drawerState) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(.7f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(text = "More Options")
                    HorizontalDivider(Modifier.padding(horizontal = 50.dp))
                    Spacer(Modifier.height(0.5.dp))
                    HorizontalDivider(Modifier.padding(horizontal = 50.dp))
                    Spacer(Modifier.height(20.dp))
                    Text(text = "User Profile", modifier = Modifier.fillMaxWidth(.8f))
                    Spacer(Modifier.height(8.dp))
                    HorizontalDivider()
                    Spacer(Modifier.height(8.dp))
                    Text(text = "User Settings", modifier = Modifier.fillMaxWidth(.8f))
                    Spacer(Modifier.height(8.dp))
                    HorizontalDivider()
                    Spacer(Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .clickable {
                                onEvent(ChannelListScreenEvent.OnClickLogout)
                            }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.Logout,
                            contentDescription = "Log Out",
                            tint = MaterialTheme.colorScheme.error
                        )
                        Spacer(Modifier.width(5.dp))
                        Text(text = "Log Out", color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    ) {
        ChatTheme(
            shapes = StreamShapes.defaultShapes().copy(
                avatar = RoundedCornerShape(25),
                header = RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp)
            ),
            colors = if (isSystemInDarkTheme()) StreamColors.defaultDarkColors().copy(
                appBackground = MaterialTheme.colorScheme.background
            ) else StreamColors.defaultColors().copy(
                appBackground = MaterialTheme.colorScheme.background
            )
        ) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
            ) { paddings ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddings),
                    contentAlignment = Alignment.Center
                ) {
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(ChatTheme.shapes.header)
                                .background(MaterialTheme.colorScheme.secondaryContainer)
                                .padding(vertical = 8.dp, horizontal = 8.dp)
                                .wrapContentHeight(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .border(
                                        width = 1.dp,
                                        color = MaterialTheme.colorScheme.background.copy(
                                            red = .5f,
                                            green = .5f,
                                            blue = .5f
                                        ),
                                        shape = ChatTheme.shapes.avatar
                                    )
                                    .clip(ChatTheme.shapes.avatar)
                                    .background(
                                        MaterialTheme.colorScheme.primary.copy(
                                            alpha = .6f,
                                            red = .3f
                                        )
                                    )
                                    .clickable {
                                        scope.launch {
                                            drawerState.open()
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = userId.take(2).uppercase(),
                                    color = MaterialTheme.colorScheme.onPrimary.copy(
                                        green = .9f
                                    ),
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.Center
                                )
                            }
                            Text(
                                text = "Channel List",
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.titleLarge
                            )
                            IconButton(
                                onClick = {
                                    onEvent(ChannelListScreenEvent.OnClickCreateNewChannel)
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Create,
                                    contentDescription = "Add New Channel"
                                )
                            }
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .clip(RoundedCornerShape(50))
                        ) {
                            OutlinedTextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp)
                                    .height(48.dp),
                                value = "",
                                onValueChange = {

                                },
                                shape = RoundedCornerShape(50)
                            )
                        }
                        ChannelsScreen(
                            onBackPressed = { onDismiss.invoke(false) },
                            isShowingHeader = false,
                            onChannelClick = {
                                //println(it)
                                onNavigate.invoke(it.cid, it.name)
                            }
                        )
                    }
                    if (uiState.showDialog) {
                        ChannelDialog(dismiss = {
                            if (it == null) {
                                onEvent(ChannelListScreenEvent.DisMissDialog)
                            } else {
                                onEvent(ChannelListScreenEvent.OnCreateNewChannel(it))
                            }
                        })
                    }
                }
            }
        }
    }
}

@Composable
private fun ChannelDialog(dismiss: (String?) -> Unit) {
    var channelName by remember {
        mutableStateOf("")
    }
    Dialog(
        properties = DialogProperties(
            dismissOnClickOutside = false,
            dismissOnBackPress = false,
        ),
        onDismissRequest = {
            //dismiss(null)
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(10)
                )
                .clip(RoundedCornerShape(10))
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier
                    .padding(top = 10.dp, end = 10.dp)
                    .size(24.dp)
                    .align(Alignment.TopEnd)
                    .clickable {
                        dismiss(null)
                    },
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                tint = MaterialTheme.colorScheme.onBackground
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Create Channel")
                HorizontalDivider(Modifier.padding(horizontal = 50.dp))
                Spacer(Modifier.height(0.5.dp))
                HorizontalDivider(Modifier.padding(horizontal = 50.dp))
                Spacer(Modifier.height(20.dp))
                OutlinedTextField(
                    value = channelName,
                    onValueChange = {
                        channelName = it
                    },
                    placeholder = {
                        Text(text = "Enter Channel Name")
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        errorContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                    )
                )
                Spacer(Modifier.height(10.dp))
                Button(
                    onClick = {
                        dismiss(channelName)
                    },
                    shape = RoundedCornerShape(25)
                ) {
                    Text(text = "Create".uppercase())
                }
            }
        }
    }
}