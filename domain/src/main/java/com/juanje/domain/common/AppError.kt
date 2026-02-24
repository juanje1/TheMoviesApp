package com.juanje.domain.common

sealed class AppError(message: String? = null): Throwable(message) {
    data object Network: AppError() { private fun readResolve(): Any = Network }
    data object Database: AppError() { private fun readResolve(): Any = Database }
    data class Unexpected(val errorMessage: String): AppError(errorMessage)

    companion object {
        const val UNKNOWN_MESSAGE = "Unknown error"
    }
}