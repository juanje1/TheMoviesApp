package com.juanje.themoviesapp.ui.screens.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.juanje.domain.dataclasses.User
import com.juanje.themoviesapp.R
import com.juanje.themoviesapp.common.showMessage
import com.juanje.themoviesapp.ui.screens.common.fields.email
import com.juanje.themoviesapp.ui.screens.common.fields.firstName
import com.juanje.themoviesapp.ui.screens.common.fields.lastName
import com.juanje.themoviesapp.ui.screens.common.fields.password
import com.juanje.themoviesapp.ui.screens.common.fields.userName

@Composable
fun RegisterScreen(
    onRegister: () -> Unit,
    onLogin: () -> Unit
) {
    val registerViewModel: RegisterViewModel = hiltViewModel()
    val registerState by registerViewModel.state.collectAsState()

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(registerState.actionFinished) {
        if (registerState.actionFinished) {
            if (registerState.userValid) {
                onRegister()
            } else {
                showMessage(coroutineScope, snackBarHostState, context.getString(R.string.error_register))
            }
            registerViewModel.resetActionFinished()
        }
    }

    LaunchedEffect(registerState.error) {
        registerState.error?.let { resId ->
            showMessage(coroutineScope, snackBarHostState, context.getString(resId))
            registerViewModel.resetError()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .consumeWindowInsets(innerPadding)
        ) {
            Image(
                painter = painterResource(id = R.drawable.register),
                contentDescription = context.getString(R.string.register_image_description),
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(R.dimen.image_height_medium))
            )
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = dimensionResource(R.dimen.padding_image_medium)),
                color = Color.White,
                shape = RoundedCornerShape(
                    topStartPercent = dimensionResource(R.dimen.surface_rounded_corner).value.toInt(),
                    topEndPercent = dimensionResource(R.dimen.surface_rounded_corner).value.toInt()
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .imePadding()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spaced_by))
                ) {
                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacer_large)))
                    Text(
                        text = context.getString(R.string.register_title_text),
                        style = MaterialTheme.typography.headlineLarge.copy(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacer_medium)))

                    val userName = userName(registerViewModel)
                    val firstName = firstName(registerViewModel)
                    val lastName = lastName(registerViewModel)
                    val email = email(registerViewModel)
                    val password = password(registerViewModel)

                    RegisterAction(
                        onCancel = onLogin,
                        registerViewModel = registerViewModel,
                        user = User(userName, firstName, lastName, email, password)
                    )
                }
            }
        }
    }
}