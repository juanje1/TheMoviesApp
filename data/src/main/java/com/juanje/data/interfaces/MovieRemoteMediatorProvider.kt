package com.juanje.data.interfaces

import androidx.paging.ExperimentalPagingApi
import androidx.paging.RemoteMediator

@OptIn(ExperimentalPagingApi::class)
interface MovieRemoteMediatorProvider {
    fun <T : Any> getMediator(userName: String, category: String): RemoteMediator<Int, T>
}