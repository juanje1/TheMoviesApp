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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.juanje.domain.RegistrationField
import com.juanje.themoviesapp.R
import com.juanje.themoviesapp.ui.screens.register.RegisterViewModel
import kotlinx.coroutines.launch

@Composable
fun userName(registerViewModel: RegisterViewModel): String {
    var userNameText by rememberSaveable { mutableStateOf("") }

    val stateRegister by registerViewModel.state.collectAsState()

    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = dimensionResource(R.dimen.padding_small))
            .padding(horizontal = dimensionResource(R.dimen.padding_large))
            .testTag(context.getString(R.string.register_user_name_test)),
        value = userNameText,
        onValueChange = {
            userNameText = it
            scope.launch {
                registerViewModel.onFieldChanged(RegistrationField.USER_NAME, userNameText)
            }
        },
        label = { Text(text = context.getString(R.string.register_user_name_label)) },
        shape = RoundedCornerShape(dimensionResource(R.dimen.shape_rounded_corner_small)),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Down) }
        ),
        trailingIcon = {
            if (stateRegister.errorMessages.containsKey(RegistrationField.USER_NAME))
                Icon(
                    imageVector = Icons.Filled.Error,
                    contentDescription = context.getString(R.string.register_error_field_description),
                    tint = MaterialTheme.colorScheme.error
                )
        },
        isError = stateRegister.errorMessages.containsKey(RegistrationField.USER_NAME)
    )
    if (stateRegister.errorMessages.containsKey(RegistrationField.USER_NAME)) {
        Text(
            text = stateRegister.errorMessages[RegistrationField.USER_NAME]?.let { stringResource(it) } ?: "",
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(start = dimensionResource(R.dimen.padding_medium))
        )
    }

    return userNameText
}