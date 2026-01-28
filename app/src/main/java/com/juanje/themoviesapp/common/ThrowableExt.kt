package com.juanje.themoviesapp.common

import com.juanje.domain.common.AppError
import com.juanje.themoviesapp.R

fun Throwable.toErrorRes(): Int = when (this) {
    is AppError.Network -> R.string.error_internet
    is AppError.Database -> R.string.error_database
    else -> R.string.error_unknown
}