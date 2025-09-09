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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.juanje.themoviesapp.R
import com.juanje.themoviesapp.ui.screens.register.RegisterViewModel

@Composable
fun email(registerViewModel: RegisterViewModel): String {
    var emailText by rememberSaveable { mutableStateOf("") }
    var isEdited by rememberSaveable { mutableStateOf(false) }

    val stateRegister by registerViewModel.state.collectAsState()

    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    if (isEdited) {
        isEdited = false
        registerViewModel.checkEmailValid(emailText)
    }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = dimensionResource(R.dimen.padding_small))
            .padding(horizontal = dimensionResource(R.dimen.padding_large)),
        value = emailText,
        onValueChange = {
            emailText = it
            isEdited = true
        },
        label = { Text(text = context.getString(R.string.register_email_label)) },
        shape = RoundedCornerShape(dimensionResource(R.dimen.shape_rounded_corner_small)),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Down) }
        ),
        trailingIcon = {
            if (stateRegister.errorMessages[context.getString(R.string.register_email_error_messages)]?.isNotEmpty() == true)
                Icon(
                    imageVector = Icons.Filled.Error,
                    contentDescription = context.getString(R.string.register_email_error_field_description),
                    tint = MaterialTheme.colorScheme.error
                )
        },
        isError = stateRegister.errorMessages[context.getString(R.string.register_email_error_messages)]?.isNotEmpty() == true
    )
    if (stateRegister.errorMessages[context.getString(R.string.register_email_error_messages)]?.isNotEmpty() == true) {
        Text(
            text = stateRegister.errorMessages[context.getString(R.string.register_email_error_messages)] ?: "",
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(start = dimensionResource(R.dimen.padding_medium))
        )
    }

    return emailText
}