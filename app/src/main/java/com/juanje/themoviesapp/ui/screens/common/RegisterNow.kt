package com.juanje.themoviesapp.ui.screens.common

import android.graphics.Color.parseColor
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.juanje.themoviesapp.R

@Composable
fun RegisterNow(onRegisterClick: () -> Unit) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = dimensionResource(R.dimen.padding_large)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .height(dimensionResource(R.dimen.line_height))
                .weight(context.getString(R.string.line_weight).toFloat())
                .background(Color(parseColor(context.getString(R.string.color_text_register_now))))
        )
        Text(
            text = context.getString(R.string.or_register_now),
            fontSize = dimensionResource(R.dimen.font_size_xsmall).value.sp,
            modifier = Modifier
                .padding(
                    start = dimensionResource(R.dimen.padding_small),
                    end = dimensionResource(R.dimen.padding_small)
                ),
            color = Color(parseColor(context.getString(R.string.color_text_register_now))),
        )
        Box(
            modifier = Modifier
                .height(dimensionResource(R.dimen.line_height))
                .weight(context.getString(R.string.line_weight).toFloat())
                .background(Color(parseColor(context.getString(R.string.color_text_register_now))))
        )
    }
    Button(
        onClick = { onRegisterClick() },
        modifier = Modifier
            .padding(
                top = dimensionResource(R.dimen.padding_medium),
                bottom = dimensionResource(R.dimen.padding_medium)
            )
            .fillMaxWidth()
            .height(dimensionResource(R.dimen.button_height)),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color(
                parseColor(context.getString(R.string.color_background_button))
            )
        ),
        shape = RoundedCornerShape(dimensionResource(R.dimen.rounded_corner_shape_small))
    ) {
        Text(
            text = context.getString(R.string.register_button),
            color = Color.White,
            fontSize = dimensionResource(R.dimen.font_size_medium).value.sp,
            fontWeight = FontWeight.Bold
        )
    }
}