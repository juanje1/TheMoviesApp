package com.juanje.domain.common

sealed class AppError: Throwable() {
    data object Network: AppError()
    data object Database: AppError()
    data class Unexpected(val errorMessage: String): AppError()
}