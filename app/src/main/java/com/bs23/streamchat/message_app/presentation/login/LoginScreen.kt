package com.bs23.streamchat.message_app.presentation.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bs23.streamchat.core.presentation.components.AppTextField

@Composable
fun LoginScreenContent(
    uiState: LoginScreenUiState,
    onEvent: (LoginScreenEvent) -> Unit,
    gotoSignUp: () -> Unit = {},
){
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ){ paddings ->
        Box(
            modifier = Modifier.fillMaxSize().padding(paddings).imePadding(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 50.dp),
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

                AppTextField(
                    state = uiState.email,
                    label = "Email Address",
                    placeholder = "Enter Email Address",
                    keyboardType = KeyboardType.Email,
                    singleLine = true,
                    onValueChange = {
                        onEvent(LoginScreenEvent.EmailChanged(it))
                    }
                )

                AppTextField(
                    state = uiState.password,
                    label = "Password",
                    placeholder = "Enter Password",
                    singleLine = true,
                    imeAction = ImeAction.Done,
                    onValueChange = {
                        onEvent(LoginScreenEvent.PasswordChanged(it))
                    }
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
                TextButton(
                    onClick = gotoSignUp
                ) {
                    Text(text = "Don't have an account? Sign Up")
                }
            }
        }
    }
}