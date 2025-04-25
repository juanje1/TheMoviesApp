package com.juanje.themoviesapp.ui.screens.common.fields

import android.graphics.Color.parseColor
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
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
import androidx.compose.ui.unit.sp
import com.juanje.themoviesapp.R

@Composable
fun EmailLogin(): String {
    var textEmailLogin by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current

    Text(
        text = context.getString(R.string.register_email),
        fontSize = dimensionResource(R.dimen.font_size_small).value.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .padding(top = dimensionResource(R.dimen.padding_medium)),
        color = Color.Black
    )
    TextField(
        value = textEmailLogin,
        onValueChange = {
            textEmailLogin = it
        },
        label = { Text(text = context.getString(R.string.register_email_label)) },
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
            .padding(top = dimensionResource(R.dimen.padding_small))
    )
    return textEmailLogin
}