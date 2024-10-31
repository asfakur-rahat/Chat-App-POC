package com.bs23.streamchat.core.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

/**
 * A customizable text input field.
 *
 * A wrapper around [OutlinedTextField] with additional parameters for label, placeholder, supporting text, and styling.
 *
 * @param modifier Optional modifier to adjust the layout or style of the text field
 * @param state Required string for holding the text field's current value
 * @param label Optional label for the text field
 * @param placeholder Optional placeholder text for the text field
 * @param supportingText Optional text displayed below the text field for helper or error messages
 * @param supportingTextStyle Optional style for supporting text (default is Material error color)
 * @param visualTransformation Optional visual transformation, e.g., password masking (default is None)
 * @param imeAction Optional IME action (default is Next)
 * @param keyboardType Optional keyboard type, e.g., text, number, password (default is Text)
 * @param singleLine Optional flag to set single-line input (default is false)
 * @param onValueChange Required callback triggered when the input value changes
 *
 * @sample SampleTextField
 * @see OutlinedTextField
 * @see KeyboardOptions
 * @see VisualTransformation
 *
 * @author Md Asfakur Rahat
 */
@Composable
fun AppTextField(
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    supportingText: String? = null,
    supportingTextStyle: TextStyle = TextStyle(color = MaterialTheme.colorScheme.error),
    state: String,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    imeAction: ImeAction = ImeAction.Next,
    keyboardType: KeyboardType = KeyboardType.Text,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    singleLine: Boolean = false,
    onValueChange: (String) -> Unit = {}
){
    OutlinedTextField(
        modifier = modifier.fillMaxWidth().padding(horizontal = 8.dp),
        value = state,
        onValueChange = onValueChange,
        label = {
            label?.let{
                Text(text = label)
            }
        },
        placeholder = {
            placeholder?.let{
                Text(text = placeholder)
            }
        },
        supportingText = {
            supportingText?.let{
                Text(
                    text = supportingText,
                    style = supportingTextStyle
                )
            }
        },
        keyboardOptions = keyboardOptions.copy(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        visualTransformation = visualTransformation,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            errorContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
        ),
        singleLine = singleLine
    )
}

@Composable
private fun SampleTextField(){
    var state by remember {
        mutableStateOf("")
    }
    AppTextField(
        modifier = Modifier,
        state = state,
        label = "label",
        placeholder = "placeholder",
        supportingText = "supportingText",
        visualTransformation = PasswordVisualTransformation(),
        imeAction = ImeAction.Done,
        keyboardType = KeyboardType.Password,
        keyboardOptions = KeyboardOptions.Default,
        singleLine = true,
        onValueChange = {
            state = it
        }
    )
}