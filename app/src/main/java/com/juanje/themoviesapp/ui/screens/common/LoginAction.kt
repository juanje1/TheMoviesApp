package com.juanje.themoviesapp.ui.screens.common

import android.graphics.Color.parseColor
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.juanje.themoviesapp.R
import com.juanje.themoviesapp.common.showToast
import com.juanje.themoviesapp.ui.screens.login.LoginViewModel

@Composable
fun LoginAction(
    onLoginClick: (LoginViewModel.UiState) -> Unit,
    email: String,
    password: String
) {
    val viewModel: LoginViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    if (state.timeExecution > 0) {
        if (state.isUserValid)
            onLoginClick(state)
        else
            showToast(context, context.getString(R.string.login_error))
        viewModel.resetState()
    }

    Button(
        onClick = { viewModel.onLoginClick(email, password) },
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
            ),
        ),
        shape = RoundedCornerShape(dimensionResource(R.dimen.rounded_corner_shape_small))
    ) {
        Text(
            text = context.getString(R.string.login_button),
            color = Color.White,
            fontSize = dimensionResource(R.dimen.font_size_medium).value.sp,
            fontWeight = FontWeight.Bold
        )
    }
}