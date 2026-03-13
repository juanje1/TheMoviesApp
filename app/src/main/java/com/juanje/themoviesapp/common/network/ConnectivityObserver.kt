package com.juanje.themoviesapp.common.network

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {
    fun observe(): Flow<Boolean>
    fun isConnected(): Boolean
}