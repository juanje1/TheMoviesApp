package com.juanje.themoviesapp.ui.screens.common

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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.juanje.themoviesapp.ui.navigation.Navigation.Home
import com.juanje.themoviesapp.ui.screens.login.LoginViewModel

@Composable
fun LoginAction(
    navController: NavController,
    email: String,
    password: String
) {
    val viewModel: LoginViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()

    if (state.timeExecution > 0) {
        if (state.isUserValid)
            navController.navigate("${Home}/${state.user!!.userName}")
        else
            Toast.makeText(
                navController.context,
                "Email/Password not valid",
                Toast.LENGTH_LONG
            ).show()
        viewModel.resetState()
    }

    Button(
        onClick = { viewModel.onLoginClick(email, password) },
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
            text = "Login",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
