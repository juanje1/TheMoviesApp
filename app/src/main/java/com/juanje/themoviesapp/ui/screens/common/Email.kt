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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.juanje.themoviesapp.ui.screens.register.RegisterViewModel

@Composable
fun Email(viewModel: RegisterViewModel): String {
    val state by viewModel.state.collectAsState()
    var textEmail by rememberSaveable { mutableStateOf("") }
    var isEdited by rememberSaveable { mutableStateOf(false) }

    if (isEdited) {
        isEdited = false
        viewModel.checkEmailValid(textEmail)
    }
    Text(
        text = "Email",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .padding(top = 16.dp),
        color = Color.Black
    )
    TextField(
        value = textEmail,
        onValueChange = {
            textEmail = it
            isEdited = true
        },
        label = { Text(text = "example@gmail.com") },
        shape = RoundedCornerShape(10.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            backgroundColor = Color.White,
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            textColor = Color(parseColor("#5E5E5E")),
            unfocusedLabelColor = Color(parseColor("#5E5E5E"))
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        trailingIcon = {
            if (state.errorMessages["Email"]?.isNotEmpty() == true)
                Icon(
                    Icons.Filled.Error,
                    "Error",
                    tint = MaterialTheme.colors.error
                )
        },
        isError = state.errorMessages["Email"]?.isNotEmpty() == true
    )
    if (state.errorMessages["Email"]?.isNotEmpty() == true) {
        Text(
            text = state.errorMessages["Email"] ?: "",
            color = MaterialTheme.colors.error,
            style = MaterialTheme.typography.caption,
            modifier = Modifier
                .padding(start = 16.dp)
        )
    }
    return textEmail
}