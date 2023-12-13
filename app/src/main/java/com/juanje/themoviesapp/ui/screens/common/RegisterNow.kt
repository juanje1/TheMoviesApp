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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.juanje.themoviesapp.ui.navigation.Navigation

@Composable
fun RegisterNow(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .height(1.dp)
                .weight(1f)
                .background(Color(parseColor("#4D4D4D")))
        )
        Text(
            text = "Or Register Now",
            fontSize = 14.sp,
            modifier = Modifier
                .padding(start = 8.dp, end = 8.dp),
            color = Color(parseColor("#4D4D4D"))
        )
        Box(
            modifier = Modifier
                .height(1.dp)
                .weight(1f)
                .background(Color(parseColor("#4D4D4D")))
        )
    }
    Button(
        onClick = { navController.navigate(Navigation.Register) },
        modifier = Modifier
            .padding(top = 16.dp, bottom = 16.dp)
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