package com.juanje.themoviesapp.ui.screens.common

import com.juanje.themoviesapp.common.utils.AppIdlingResource

class FakeAppIdlingResource: AppIdlingResource {
    override fun increment() {}
    override fun decrement() {}
}