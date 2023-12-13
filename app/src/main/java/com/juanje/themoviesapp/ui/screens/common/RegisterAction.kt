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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
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

    if (state.timeExecution > 0) {
        if (state.userValid) {
            showToast(navController.context, "User created successfully")
            navController.navigate(Login)
        }
        else {
            showToast(navController.context, "Error creating the user")
        }
        viewModel.resetState()
    }

    Button(
        onClick = { viewModel.onRegisterClick(userName, firstName, lastName, email, password) },
        modifier = Modifier
            .padding(top = 32.dp, bottom = 16.dp)
            .fillMaxWidth()
            .height(55.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color(parseColor("#FA951A")),
        ),
        shape = RoundedCornerShape(10.dp)
    ) {
        Text(
            text = "Register",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

private fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}