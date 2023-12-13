package com.juanje.themoviesapp.ui.screens.register

import android.graphics.Color.parseColor
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.juanje.themoviesapp.R
import com.juanje.themoviesapp.ui.screens.common.*

@Composable
fun RegisterScreen(navController: NavController) {
    val viewModel: RegisterViewModel = hiltViewModel()

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .paint(
                painter = painterResource(id = R.drawable.background_page),
                contentScale = ContentScale.FillWidth
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        ConstraintLayout (
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            val (ref1, ref2) = createRefs()
            Text(text = "TheMoviesApp | Register",
                color = Color.White,
                modifier = Modifier
                    .padding(top = 16.dp, start = 16.dp)
                    .constrainAs(ref1) {
                        linkTo(parent.top, ref2.top, bias = 0.6f)
                        linkTo(parent.start, parent.end, bias = 0f)
                    },
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(600.dp)
                    .verticalScroll(rememberScrollState())
                    .constrainAs(ref2) {
                        bottom.linkTo(parent.bottom)
                    }
                    .background(
                        color = Color(parseColor("#E0E0E0")),
                        shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
                    )
                    .padding(32.dp)
            ) {
                RegisterAction(
                    navController,
                    viewModel,
                    UserName(viewModel),
                    FirstName(viewModel),
                    LastName(viewModel),
                    Email(viewModel),
                    Password(viewModel)
                )
            }
        }
    }
}