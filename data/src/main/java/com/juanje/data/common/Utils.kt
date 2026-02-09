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
    } catch (e: Exception) {
        if (e is CancellationException) throw e

        throw when (e) {
            is UnknownHostException -> AppError.Network
            is SocketTimeoutException -> AppError.Network
            is IOException -> AppError.Network
            is SQLiteException -> AppError.Database
            else -> AppError.Unexpected(e.message ?: "Unknown error")
        }
    }
}

fun <T> Flow<T>.asAppError(): Flow<T> =
    this.catch { e ->
        if (e is AppError) throw e

        throw when (e) {
            is UnknownHostException -> AppError.Network
            is SocketTimeoutException -> AppError.Network
            is IOException -> AppError.Network
            is SQLiteException -> AppError.Database
            else -> AppError.Unexpected(e.message ?: "Unknown error")
        }
    }