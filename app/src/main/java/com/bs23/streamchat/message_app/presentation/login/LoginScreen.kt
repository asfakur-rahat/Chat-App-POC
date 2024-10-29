package com.bs23.streamchat.message_app.presentation.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bs23.streamchat.core.presentation.base.BaseUiState
import com.bs23.streamchat.core.presentation.components.EmptyScreen
import com.bs23.streamchat.core.presentation.components.ErrorScreen
import com.bs23.streamchat.core.presentation.components.LoadingScreen
import com.bs23.streamchat.core.presentation.util.cast
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = koinViewModel(),
    goChannelScreen: (String) -> Unit,
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    when(uiState){
        is BaseUiState.Data -> {
            val data = uiState.cast<BaseUiState.Data<LoginScreenUiState>>().data
            LoginScreenContent(
                uiState = data,
                onEvent = {
                    viewModel.onTriggerEvent(it)
                },
                onNavigate = {
                    goChannelScreen(it)
                }
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
fun LoginScreenContent(
    uiState: LoginScreenUiState,
    onEvent: (LoginScreenEvent) -> Unit,
    onNavigate: (String) -> Unit
){
    LaunchedEffect(uiState.isLoggedIn) {
        if(uiState.isLoggedIn){
            onNavigate(uiState.userName)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ){ paddings ->
        Box(
            modifier = Modifier.fillMaxSize().padding(paddings),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            ){
                Icon(
                    modifier = Modifier.size(64.dp),
                    imageVector = Icons.AutoMirrored.Outlined.Chat,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(text = "Login", style = MaterialTheme.typography.headlineLarge.copy(
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold
                ))
                OutlinedTextField(
                    value = uiState.userName,
                    onValueChange = {
                        onEvent(LoginScreenEvent.UserNameChanged(it))
                    },
                    label = {
                        Text(text = "Username")
                    },
                    placeholder = {
                        Text(text = "Enter Username")
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        errorContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
//                        focusedIndicatorColor = Color.Transparent,
//                        unfocusedIndicatorColor = Color.Transparent,
//                        errorIndicatorColor = Color.Transparent,
//                        disabledIndicatorColor = Color.Transparent,
                    )
                )
                Button(
                    onClick = {
                        onEvent(LoginScreenEvent.LoginButtonClicked)
                    },
                    shape = RoundedCornerShape(25)
                ) {
                    Text(text = "Login As User")
                }
                Button(
                    onClick = {
                        onEvent(LoginScreenEvent.LoginAsGuestClicked)
                    },
                    shape = RoundedCornerShape(25)
                ) {
                    Text(text = "Login As Guest")
                }
            }
        }
    }
}