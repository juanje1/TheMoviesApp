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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.juanje.domain.common.RegistrationField
import com.juanje.themoviesapp.R
import com.juanje.themoviesapp.ui.screens.register.RegisterViewModel

@Composable
fun PasswordField(
    onFieldChanged: (RegistrationField, String) -> Unit,
    registerState: RegisterViewModel.UiState
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = dimensionResource(R.dimen.padding_small))
            .padding(horizontal = dimensionResource(R.dimen.padding_large))
            .testTag(stringResource(R.string.register_password_test)),
        value = registerState.user.password,
        onValueChange = { onFieldChanged(RegistrationField.PASSWORD, it) },
        label = { Text(text = stringResource(R.string.register_password_label)) },
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
                if (passwordVisible) stringResource(R.string.register_password_hide)
                else stringResource(R.string.register_password_show)
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(imageVector = image, description)
            }
            if (registerState.errorMessages.containsKey(RegistrationField.PASSWORD))
                Icon(
                    imageVector = Icons.Filled.Error,
                    contentDescription = stringResource(R.string.register_error_field_description),
                    tint = MaterialTheme.colorScheme.error
                )
        },
        isError = registerState.errorMessages.containsKey(RegistrationField.PASSWORD)
    )
    if (registerState.errorMessages.containsKey(RegistrationField.PASSWORD)) {
        Text(
            text = registerState.errorMessages[RegistrationField.PASSWORD]?.let { stringResource(it) } ?: "",
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(start = dimensionResource(R.dimen.padding_medium))
        )
    }
}