package com.juanje.themoviesapp.common.extensions

import com.juanje.themoviesapp.common.utils.AppIdlingResource

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