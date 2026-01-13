package com.juanje.themoviesapp.ui.screens.common

import com.juanje.themoviesapp.common.AppIdlingResource

class FakeAppIdlingResource: AppIdlingResource {
    override fun increment() {}
    override fun decrement() {}
}