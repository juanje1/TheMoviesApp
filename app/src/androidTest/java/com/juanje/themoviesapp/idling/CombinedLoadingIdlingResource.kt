package com.juanje.themoviesapp.idling

import androidx.compose.ui.test.IdlingResource
import kotlinx.coroutines.flow.StateFlow

class CombinedLoadingIdlingResource(private val loadingCheckers: List<StateFlow<Boolean>>): IdlingResource {

    override val isIdleNow: Boolean
        get() = loadingCheckers.all { !it.value }
}