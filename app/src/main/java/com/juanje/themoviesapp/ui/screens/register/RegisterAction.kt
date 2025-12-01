package com.juanje.themoviesapp.ui.screens.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import com.juanje.domain.User
import com.juanje.themoviesapp.R

@Composable
fun RegisterAction(
    onRegister: () -> Unit,
    onCancel: () -> Unit,
    registerViewModel: RegisterViewModel,
    user: User
) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = dimensionResource(R.dimen.padding_large)),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        Button(
            modifier = Modifier
                .width(dimensionResource(R.dimen.button_width_small))
                .height(dimensionResource(R.dimen.button_height_medium))
                .testTag(context.getString(R.string.register_register_test)),
            onClick = { registerViewModel.onRegister(user) },
            shape = RoundedCornerShape(dimensionResource(R.dimen.shape_rounded_corner_medium))
        ) {
            Text(
                text = context.getString(R.string.register_button).uppercase()
            )
        }
        Button(
            modifier = Modifier
                .width(dimensionResource(R.dimen.button_width_small))
                .height(dimensionResource(R.dimen.button_height_medium)),
            onClick = { onCancel() },
            shape = RoundedCornerShape(dimensionResource(R.dimen.shape_rounded_corner_medium))
        ) {
            Text(
                text = context.getString(R.string.cancel_button).uppercase()
            )
        }
    }
}