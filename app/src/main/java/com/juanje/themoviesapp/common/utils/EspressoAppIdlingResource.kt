package com.juanje.themoviesapp.common.utils

import com.juanje.themoviesapp.utils.IdlingResourceProvider
import javax.inject.Inject

class EspressoAppIdlingResource @Inject constructor(): AppIdlingResource {
    override fun increment() = IdlingResourceProvider.increment()
    override fun decrement() = IdlingResourceProvider.decrement()
}