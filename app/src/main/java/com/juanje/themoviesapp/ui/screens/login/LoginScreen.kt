package com.juanje.themoviesapp.ui.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.juanje.themoviesapp.R
import com.juanje.themoviesapp.common.showMessage

@Composable
fun LoginScreen(
    onHome: (String) -> Unit,
    onRegister: () -> Unit,
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val loginState by loginViewModel.state.collectAsState()
    val infoRegisterMessage = stringResource(R.string.info_register_success)
    val errorLoginIncorrectMessage = stringResource(R.string.error_login_incorrect)
    val errorMessage = loginState.error?.let { stringResource(it) }
    val coroutineScope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        if (loginViewModel.registered)
            showMessage(coroutineScope, snackBarHostState, infoRegisterMessage)
    }

    LaunchedEffect(loginState.timeExecution) {
        if (loginState.timeExecution > 0) {
            if (loginState.isUserValid) {
                onHome(loginState.user?.userName!!)
            } else {
                showMessage(coroutineScope, snackBarHostState, errorLoginIncorrectMessage)
            }
            loginViewModel.resetState()
        }
    }

    LaunchedEffect(errorMessage) {
        errorMessage?.let { message ->
            showMessage(coroutineScope, snackBarHostState, message)
            loginViewModel.resetError()
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
                modifier = Modifier.testTag(stringResource(R.string.snack_bar_host_test))
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
        ){
            LoginItem(
                onLoginCheck = { email, password -> loginViewModel.onLogin(email, password) },
                onRegister = onRegister,
                loginState = loginState
            )
        }
    }
}