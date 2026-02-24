package com.juanje.themoviesapp.ui.screens.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
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
fun RegisterScreen(
    onRegister: () -> Unit,
    onLogin: () -> Unit,
    registerViewModel: RegisterViewModel = hiltViewModel()
) {
    val registerState by registerViewModel.state.collectAsState()
    val errorRegisterMessage = stringResource(R.string.error_register)
    val errorMessage = registerState.error?.let { stringResource(it) }
    val coroutineScope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(registerState.actionFinished) {
        if (registerState.actionFinished) {
            if (registerState.userValid) {
                onRegister()
            } else {
                showMessage(coroutineScope, snackBarHostState, errorRegisterMessage)
            }
            registerViewModel.resetActionFinished()
        }
    }

    LaunchedEffect(errorMessage) {
        errorMessage?.let { message ->
            showMessage(coroutineScope, snackBarHostState, message)
            registerViewModel.resetError()
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
                .consumeWindowInsets(padding)
        ) {
            RegisterItem(
                onRegister = { registerViewModel.onRegister() },
                onCancel = onLogin,
                onFieldChanged = { field, text -> registerViewModel.onFieldChanged(field, text) },
                registerState = registerState
            )
        }
    }
}