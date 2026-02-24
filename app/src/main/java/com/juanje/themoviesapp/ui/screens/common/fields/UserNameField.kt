package com.juanje.themoviesapp.ui.screens.common.fields

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.juanje.domain.common.RegistrationField
import com.juanje.themoviesapp.R
import com.juanje.themoviesapp.ui.screens.register.RegisterViewModel

@Composable
fun UserNameField(
    onFieldChanged: (RegistrationField, String) -> Unit,
    registerState: RegisterViewModel.UiState
) {
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = dimensionResource(R.dimen.padding_small))
            .padding(horizontal = dimensionResource(R.dimen.padding_large))
            .testTag(stringResource(R.string.register_user_name_test)),
        value = registerState.user.userName,
        onValueChange = { onFieldChanged(RegistrationField.USER_NAME, it) },
        label = { Text(text = stringResource(R.string.register_user_name_label)) },
        shape = RoundedCornerShape(dimensionResource(R.dimen.shape_rounded_corner_small)),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Down) }
        ),
        trailingIcon = {
            if (registerState.errorMessages.containsKey(RegistrationField.USER_NAME))
                Icon(
                    imageVector = Icons.Filled.Error,
                    contentDescription = stringResource(R.string.register_error_field_description),
                    tint = MaterialTheme.colorScheme.error
                )
        },
        isError = registerState.errorMessages.containsKey(RegistrationField.USER_NAME)
    )
    if (registerState.errorMessages.containsKey(RegistrationField.USER_NAME)) {
        Text(
            text = registerState.errorMessages[RegistrationField.USER_NAME]?.let { stringResource(it) } ?: "",
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(start = dimensionResource(R.dimen.padding_medium))
        )
    }
}