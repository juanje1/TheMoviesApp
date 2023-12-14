package com.juanje.themoviesapp.ui.screens.login

import android.graphics.Color.parseColor
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.juanje.themoviesapp.R
import com.juanje.themoviesapp.ui.screens.common.EmailLogin
import com.juanje.themoviesapp.ui.screens.common.LoginAction
import com.juanje.themoviesapp.ui.screens.common.PasswordLogin
import com.juanje.themoviesapp.ui.screens.common.RegisterNow

@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current

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
            Text(text = context.getString(R.string.login_title),
                color = Color.White,
                modifier = Modifier
                    .padding(
                        top = dimensionResource(R.dimen.padding_medium),
                        start = dimensionResource(R.dimen.padding_medium)
                    )
                    .constrainAs(ref1) {
                        linkTo(
                            parent.top,
                            ref2.top,
                            bias = context.getString(R.string.bias_medium).toFloat()
                        )
                        linkTo(
                            parent.start,
                            parent.end,
                            bias = context.getString(R.string.bias_small).toFloat()
                        )
                    },
                fontSize = dimensionResource(R.dimen.font_size_large).value.sp,
                fontWeight = FontWeight.Bold
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(R.dimen.cell_image_height))
                    .constrainAs(ref2) {
                        bottom.linkTo(parent.bottom)
                    }
                    .background(
                        color = Color(
                            parseColor(context.getString(R.string.color_background_login))
                        ),
                        shape = RoundedCornerShape(
                            topStart = dimensionResource(R.dimen.rounded_corner_shape_medium),
                            topEnd = dimensionResource(R.dimen.rounded_corner_shape_medium)
                        )
                    )
                    .padding(dimensionResource(R.dimen.padding_xlarge))
            ) {
                LoginAction(
                    navController,
                    EmailLogin(),
                    PasswordLogin()
                )
                RegisterNow(navController)
            }
        }
    }
}