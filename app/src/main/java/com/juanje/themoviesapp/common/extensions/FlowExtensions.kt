package com.juanje.themoviesapp.common.extensions

import com.juanje.themoviesapp.common.utils.AppIdlingResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

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