package com.juanje.data.common

import android.database.sqlite.SQLiteException
import com.juanje.domain.common.AppError
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

suspend fun <T> safeCall(call: suspend () -> T): T {
    return try {
        call()
    } catch (e: Throwable) {
        if (e is CancellationException) throw e
        throw e.toAppError()
    }
}

fun <T> Flow<T>.asAppError(): Flow<T> =
    this.catch { e ->
        if (e is CancellationException) throw e
        throw e.toAppError()
    }

fun Throwable.toAppError(): AppError {
    val rootCause = generateSequence(this) { it.cause }.last()
    if (rootCause is AppError) return rootCause

    return when (rootCause) {
        is UnknownHostException, is SocketTimeoutException, is IOException -> AppError.Network
        is SQLiteException -> AppError.Database
        else -> {
            if (rootCause.message?.contains(AppError.EAI_NODATA_ERROR) == true) {
                AppError.Network
            } else {
                AppError.Unexpected(rootCause.message ?: AppError.UNKNOWN_ERROR)
            }
        }
    }
}