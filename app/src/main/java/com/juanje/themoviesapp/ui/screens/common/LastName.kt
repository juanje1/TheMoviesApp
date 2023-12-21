package com.juanje.themoviesapp.ui.screens.common

import android.graphics.Color.parseColor
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.juanje.themoviesapp.R
import com.juanje.themoviesapp.ui.screens.register.RegisterViewModel

@Composable
fun LastName(viewModel: RegisterViewModel): String {
    val state by viewModel.state.collectAsState()
    var textLastName by rememberSaveable { mutableStateOf("") }
    var isEdited by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current

    if (isEdited) {
        isEdited = false
        viewModel.checkFieldValid(
            context.getString(R.string.lastname_error_messages),
            textLastName
        )
    }
    Text(
        text = context.getString(R.string.lastname),
        fontSize = dimensionResource(R.dimen.font_size_small).value.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .padding(top = dimensionResource(R.dimen.padding_medium)),
        color = Color.Black
    )

    TextField(
        value = textLastName,
        onValueChange = {
            textLastName = it
            isEdited = true
        },
        label = { Text(text = context.getString(R.string.lastname_label)) },
        shape = RoundedCornerShape(dimensionResource(R.dimen.rounded_corner_shape_small)),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            backgroundColor = Color.White,
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            textColor = Color(parseColor(context.getString(R.string.color_text_fields))),
            unfocusedLabelColor = Color(parseColor(context.getString(R.string.color_text_fields)))
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = dimensionResource(R.dimen.padding_small)),
        trailingIcon = {
            if (state.errorMessages[context.getString(R.string.lastname_error_messages)]?.
                isNotEmpty() == true)
                Icon(
                    Icons.Filled.Error,
                    context.getString(R.string.lastname_error_field_description),
                    tint = MaterialTheme.colors.error
                )
        },
        isError = state.errorMessages[context.getString(R.string.lastname_error_messages)]?.
            isNotEmpty() == true
    )
    if (state.errorMessages[context.getString(R.string.lastname_error_messages)]?.
        isNotEmpty() == true) {
        Text(
            text = state.errorMessages[context.getString(R.string.lastname_error_messages)] ?: "",
            color = MaterialTheme.colors.error,
            style = MaterialTheme.typography.caption,
            modifier = Modifier
                .padding(start = dimensionResource(R.dimen.padding_medium))
        )
    }
    return textLastName
}