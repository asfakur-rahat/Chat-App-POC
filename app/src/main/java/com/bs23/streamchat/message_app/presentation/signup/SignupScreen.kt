package com.bs23.streamchat.message_app.presentation.signup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bs23.streamchat.core.presentation.components.AppTextField

@Composable
fun SignUpScreenContent(
    uiState: SignupScreenUiState,
    onEvent: (SignupScreenEvent) -> Unit,
    gotoLogIn: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ){ paddings ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddings)
                .imePadding(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(horizontal = 50.dp)
                    .verticalScroll(
                        rememberScrollState()
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            ){
                Icon(
                    modifier = Modifier.size(64.dp),
                    imageVector = Icons.AutoMirrored.Outlined.Chat,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(text = "User SignUp", style = MaterialTheme.typography.headlineLarge.copy(
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold
                ))
                AppTextField(
                    state = uiState.userName,
                    onValueChange = {
                        onEvent(SignupScreenEvent.UserNameChanged(it))
                    },
                    label = "User Name",
                    placeholder = "Enter User Name",
                    supportingText = uiState.userNameError
                )
                AppTextField(
                    state = uiState.email,
                    onValueChange = {
                        onEvent(SignupScreenEvent.OnEmailChanged(it))
                    },
                    label = "Email Address",
                    placeholder = "Enter Email Address",
                    supportingText = uiState.emailError,
                    keyboardType = KeyboardType.Email
                )
                AppTextField(
                    state = uiState.password,
                    label = "Password",
                    placeholder = "Enter Password",
                    supportingText = uiState.passwordError,
                    onValueChange = {
                        onEvent(SignupScreenEvent.OnPasswordChanged(it))
                    },
                    visualTransformation = PasswordVisualTransformation()
                )
                AppTextField(
                    state = uiState.confirmPassword,
                    label = "Confirm Password",
                    placeholder = "Confirm Password",
                    supportingText = uiState.confirmPasswordError,
                    onValueChange = {
                        onEvent(SignupScreenEvent.OnConfirmPasswordChanged(it))
                    },
                    imeAction = ImeAction.Done,
                    visualTransformation = PasswordVisualTransformation()
                )
                Button(
                    onClick = {
                        onEvent(SignupScreenEvent.UserSignUp(uiState.userName,uiState.email, uiState.password, uiState.confirmPassword))
                    },
                    shape = RoundedCornerShape(25)
                ) {
                    Text(text = "Sign Up".uppercase())
                }

                TextButton(
                    onClick = gotoLogIn
                ) {
                    Text(text = "Already have an account? Log In")
                }
            }
        }
    }

}
