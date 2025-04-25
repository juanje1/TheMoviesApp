package com.juanje.themoviesapp.ui.screens.common.fields

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp
import com.juanje.themoviesapp.R

@Composable
fun PasswordLogin(): String {
    var textPasswordLogin by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current

    Text(
        text = context.getString(R.string.register_password),
        fontSize = dimensionResource(R.dimen.font_size_small).value.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .padding(top = dimensionResource(R.dimen.padding_medium)),
        color = Color.Black
    )
    TextField(
        value = textPasswordLogin,
        onValueChange = {
            textPasswordLogin = it
        },
        label = { Text(text = context.getString(R.string.register_password_label)) },
        shape = RoundedCornerShape(dimensionResource(R.dimen.rounded_corner_shape_small)),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation =
        if (passwordVisible) VisualTransformation.None
        else PasswordVisualTransformation(),
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
            val image =
                if (passwordVisible) Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff
            val description =
                if (passwordVisible) context.getString(R.string.register_password_hide)
                else context.getString(R.string.register_password_show)
            IconButton(onClick = { passwordVisible = !passwordVisible }){
                Icon(imageVector = image, description)
            }
        }
    )
    return textPasswordLogin
}