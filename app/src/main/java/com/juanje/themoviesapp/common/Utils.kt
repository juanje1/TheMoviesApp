package com.juanje.themoviesapp.common

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

const val IMAGE_ASPECT_RATIO = 2/3f
const val PAGE_THRESHOLD = 14

fun showMessage(coroutineScope: CoroutineScope, snackBarHostState: SnackbarHostState, message: String) {
    coroutineScope.launch {
        snackBarHostState.showSnackbar(message = message, duration = SnackbarDuration.Short)
    }
}

fun Throwable.isIgnorableCancellation() =
    this is CancellationException

fun createHandler(onUpdateError: (Int) -> Unit, onCleanup: () -> Unit = {}) = CoroutineExceptionHandler { _, e ->
    if (e.isIgnorableCancellation()) return@CoroutineExceptionHandler

    onUpdateError(e.toErrorRes())
    onCleanup()
}

suspend inline fun <T> trackLoading(
    idlingResource: AppIdlingResource,
    crossinline block: suspend () -> T
): T? {
    idlingResource.increment()
    return try {
        block()
    } catch (e: Exception) {
        throw e
    } finally {
        idlingResource.decrement()
    }
}

fun <T> Flow<T>.trackFlow(
    idlingResource: AppIdlingResource,
    onFirstEmit: () -> Unit = {}
): Flow<T> {
    var hasDecremented = false

    return this.onStart {
        idlingResource.increment()
    }.onEach {
        if (!hasDecremented) {
            hasDecremented = true
            onFirstEmit()
            idlingResource.decrement()
        }
    }.onCompletion {
        if (!hasDecremented) {
            hasDecremented = true
            idlingResource.decrement()
        }
    }
}