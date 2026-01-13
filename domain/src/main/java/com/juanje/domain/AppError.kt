package com.juanje.domain

sealed class AppError: Throwable() {
    data object Network: AppError()
    data object Database: AppError()
    data class Unexpected(val errorMessage: String): AppError()
}