package com.juanje.themoviesapp.common

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {
    fun observe(): Flow<Boolean>
}