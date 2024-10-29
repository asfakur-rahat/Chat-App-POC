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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
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
fun SignUpScreen(
    viewModel: SignupViewModel = koinViewModel(),
    onNavigate: (String) -> Unit,
    gotoLogIn: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    when(uiState){
        is BaseUiState.Data -> {
            val data = uiState.cast<BaseUiState.Data<SignupScreenUiState>>().data
            SignUpScreenContent(
                uiState = data,
                onEvent = viewModel::onTriggerEvent,
                onNavigate = onNavigate,
                gotoLogIn = gotoLogIn
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
fun SignUpScreenContent(
    uiState: SignupScreenUiState,
    onEvent: (SignupScreenEvent) -> Unit,
    onNavigate: (String) -> Unit,
    gotoLogIn: () -> Unit
) {
    LaunchedEffect(uiState.signUpSuccessful) {
        if(uiState.signUpSuccessful) {
            onNavigate(uiState.userID)
        }
    }

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

                OutlinedTextField(
                    value = uiState.userName,
                    onValueChange = {
                        onEvent(SignupScreenEvent.UserNameChanged(it))
                    },
                    label = {
                        Text(text = "User Name")
                    },
                    supportingText = {
                        uiState.userNameError?.let{
                            Text(text = uiState.userNameError, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    placeholder = {
                        Text(text = "Enter User Name")
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        errorContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                    )
                )

                OutlinedTextField(
                    value = uiState.email,
                    onValueChange = {
                        onEvent(SignupScreenEvent.OnEmailChanged(it))
                    },
                    label = {
                        Text(text = "Email")
                    },
                    supportingText = {
                        uiState.emailError?.let{
                            Text(text = uiState.emailError, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    placeholder = {
                        Text(text = "Enter Email")
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        errorContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                    )
                )
                OutlinedTextField(
                    value = uiState.password,
                    onValueChange = {
                        onEvent(SignupScreenEvent.OnPasswordChanged(it))
                    },
                    label = {
                        Text(text = "Password")
                    },
                    supportingText = {
                        uiState.passwordError?.let{
                            Text(text = uiState.passwordError, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    visualTransformation = PasswordVisualTransformation(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        errorContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                    )
                )
                OutlinedTextField(
                    value = uiState.confirmPassword,
                    onValueChange = {
                        onEvent(SignupScreenEvent.OnConfirmPasswordChanged(it))
                    },
                    label = {
                        Text(text = "Confirm Password")
                    },
                    supportingText = {
                        uiState.confirmPasswordError?.let{
                            Text(text = uiState.confirmPasswordError, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                    visualTransformation = PasswordVisualTransformation(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        errorContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                    )
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
