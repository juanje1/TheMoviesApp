package com.juanje.themoviesapp.ui.screens.common

import android.graphics.Color.parseColor
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PasswordLogin(): String {
    var textPasswordLogin by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    Text(
        text = "Password",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .padding(top = 16.dp),
        color = Color.Black
    )
    TextField(
        value = textPasswordLogin,
        onValueChange = {
            textPasswordLogin = it
        },
        label = { Text(text = "Type your Password") },
        shape = RoundedCornerShape(10.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation =
        if (passwordVisible) VisualTransformation.None
        else PasswordVisualTransformation(),
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
            val image =
                if (passwordVisible) Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff
            val description =
                if (passwordVisible) "Hide password"
                else "Show password"
            IconButton(onClick = { passwordVisible = !passwordVisible }){
                Icon(imageVector = image, description)
            }
        }
    )
    return textPasswordLogin
}