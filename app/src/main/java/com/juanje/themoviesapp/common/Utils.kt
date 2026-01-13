package com.juanje.themoviesapp.common

import android.content.Context
import androidx.compose.material3.SnackbarHostState
import com.juanje.themoviesapp.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

const val IMAGE_ASPECT_RATIO = 2/3f
const val PAGE_THRESHOLD = 16

fun showMessage(coroutineScope: CoroutineScope, snackBarHostState: SnackbarHostState, message: String, context: Context) {
    coroutineScope.launch {
        snackBarHostState.showSnackbar(
            message = message,
            actionLabel = context.getString(R.string.snack_bar_action_label)
        )
    }
}

fun <T> Flow<T>.trackLoading(idlingResource: AppIdlingResource): Flow<T> {
    return this
        .onStart { idlingResource.increment() }
        .onCompletion { idlingResource.decrement() }
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