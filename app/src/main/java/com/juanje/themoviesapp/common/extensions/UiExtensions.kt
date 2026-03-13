package com.juanje.themoviesapp.common.extensions

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun showMessage(coroutineScope: CoroutineScope, snackBarHostState: SnackbarHostState, message: String) {
    coroutineScope.launch {
        snackBarHostState.showSnackbar(message = message, duration = SnackbarDuration.Short)
    }
}