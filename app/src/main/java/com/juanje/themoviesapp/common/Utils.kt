package com.juanje.themoviesapp.common

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

const val IMAGE_ASPECT_RATIO = 2/3f
const val PAGE_THRESHOLD = 16

fun showMessage(coroutineScope: CoroutineScope, snackBarHostState: SnackbarHostState, message: String) {
    coroutineScope.launch {
        snackBarHostState.showSnackbar(message = message, duration = SnackbarDuration.Short)
    }
}

suspend inline fun <T> trackLoading(
    idlingResource: AppIdlingResource,
    crossinline onError: (Throwable) -> Unit = {},
    crossinline onFinally: () -> Unit = {},
    crossinline block: suspend () -> T
): T? {
    idlingResource.increment()
    return try {
        block()
    } catch (e: Exception) {
        onError(e)
        null
    } finally {
        onFinally()
        idlingResource.decrement()
    }
}