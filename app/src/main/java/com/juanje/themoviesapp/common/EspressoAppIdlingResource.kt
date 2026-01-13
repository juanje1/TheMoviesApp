package com.juanje.themoviesapp.common

import com.juanje.themoviesapp.utils.IdlingResourceProvider

class EspressoAppIdlingResource: AppIdlingResource {
    override fun increment() = IdlingResourceProvider.increment()
    override fun decrement() = IdlingResourceProvider.decrement()
}