package com.juanje.themoviesapp.ui.screens.common

import android.content.Context
import android.graphics.Color.parseColor
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.juanje.themoviesapp.R
import com.juanje.themoviesapp.ui.navigation.Navigation.Login
import com.juanje.themoviesapp.ui.screens.register.RegisterViewModel

@Composable
fun RegisterAction(
    navController: NavController,
    viewModel: RegisterViewModel,
    userName: String,
    firstName: String,
    lastName: String,
    email: String,
    password: String
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    if (state.timeExecution > 0) {
        if (state.userValid) {
            showToast(navController.context, context.getString(R.string.register_success))
            navController.navigate(Login)
        }
        else {
            showToast(navController.context, context.getString(R.string.register_error))
        }
        viewModel.resetState()
    }

    Button(
        onClick = { viewModel.onRegisterClick(userName, firstName, lastName, email, password) },
        modifier = Modifier
            .padding(
                top = dimensionResource(R.dimen.padding_xlarge),
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

private fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}