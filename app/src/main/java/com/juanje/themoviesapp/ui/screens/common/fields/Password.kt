package com.juanje.themoviesapp.ui.screens.common.fields

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.juanje.themoviesapp.R
import com.juanje.themoviesapp.ui.screens.register.RegisterViewModel

@Composable
fun password(registerViewModel: RegisterViewModel): String {
    var passwordText by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var isEdited by rememberSaveable { mutableStateOf(false) }

    val stateRegister by registerViewModel.state.collectAsState()

    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    if (isEdited) {
        isEdited = false
        registerViewModel.checkPasswordValid(passwordText)
    }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = dimensionResource(R.dimen.padding_small))
            .padding(horizontal = dimensionResource(R.dimen.padding_large)),
        value = passwordText,
        onValueChange = {
            passwordText = it
            isEdited = true
        },
        label = { Text(text = context.getString(R.string.register_password_label)) },
        shape = RoundedCornerShape(dimensionResource(R.dimen.shape_rounded_corner_small)),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = { focusManager.clearFocus() }
        ),
        visualTransformation =
            if (passwordVisible) VisualTransformation.None
            else PasswordVisualTransformation(),
        trailingIcon = {
            val image =
                if (passwordVisible) Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff
            val description =
                if (passwordVisible) context.getString(R.string.register_password_hide)
                else context.getString(R.string.register_password_show)
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(imageVector = image, description)
            }
            if (stateRegister.errorMessages[context.getString(R.string.register_password_error_messages)]?.isNotEmpty() == true)
                Icon(
                    imageVector = Icons.Filled.Error,
                    contentDescription = context.getString(R.string.register_password_error_field_description),
                    tint = MaterialTheme.colorScheme.error
                )
        },
        isError = stateRegister.errorMessages[context.getString(R.string.register_password_error_messages)]?.isNotEmpty() == true
    )
    if (stateRegister.errorMessages[context.getString(R.string.register_password_error_messages)]?.isNotEmpty() == true) {
        Text(
            text = stateRegister.errorMessages[context.getString(R.string.register_password_error_messages)] ?: "",
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(start = dimensionResource(R.dimen.padding_medium))
        )
    }

    return passwordText
}