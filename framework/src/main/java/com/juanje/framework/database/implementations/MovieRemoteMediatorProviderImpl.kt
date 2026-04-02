package com.juanje.framework.database.implementations

import androidx.paging.ExperimentalPagingApi
import androidx.paging.RemoteMediator
import com.juanje.data.interfaces.MovieRemoteMediatorProvider
import com.juanje.framework.database.paging.MovieRemoteMediatorFactory
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class MovieRemoteMediatorProviderImpl @Inject constructor(private val factory: MovieRemoteMediatorFactory) :
    MovieRemoteMediatorProvider {

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> getMediator(userName: String, category: String): RemoteMediator<Int, T> =
        factory.create(userName, category) as RemoteMediator<Int, T>
}