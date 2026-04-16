package com.juanje.themoviesapp.common.extensions

import com.juanje.data.common.toAppError
import com.juanje.domain.common.AppError
import com.juanje.themoviesapp.R
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlin.coroutines.cancellation.CancellationException

fun Throwable.toErrorRes(): Int = when (this.toAppError()) {
    is AppError.Network -> R.string.error_internet
    is AppError.Database -> R.string.error_database
    else -> R.string.error_unknown
}

fun Throwable.isIgnorableCancellation() =
    this is CancellationException

fun createHandler(onUpdateError: (Int) -> Unit, onCleanup: () -> Unit = {}) = CoroutineExceptionHandler { _, e ->
    if (e.isIgnorableCancellation()) return@CoroutineExceptionHandler

    onUpdateError(e.toErrorRes())
    onCleanup()
}