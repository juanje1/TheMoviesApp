package com.juanje.framework.database.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.juanje.data.datasources.MovieLocalDataSource
import com.juanje.data.datasources.MovieRemoteDataSource
import com.juanje.data.datasources.PageLocalDataSource
import com.juanje.data.repositories.MovieRepositoryImpl.Companion.PAGE_SIZE
import com.juanje.domain.dataclasses.Page
import com.juanje.framework.database.TheMoviesAppDatabase
import com.juanje.framework.database.dataclasses.MovieDatabase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import javax.inject.Named

@OptIn(ExperimentalPagingApi::class)
class MovieRemoteMediator @AssistedInject constructor(
    private val db: TheMoviesAppDatabase,
    private val movieLocalDataSource: MovieLocalDataSource,
    private val movieRemoteDataSource: MovieRemoteDataSource,
    private val pageLocalDataSource: PageLocalDataSource,
    @Assisted("userName") private val userName: String,
    @Assisted("category") private val category: String,
    @Named("apiKey") private val apiKey: String
) : RemoteMediator<Int, MovieDatabase>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, MovieDatabase>): MediatorResult {
        val nextPage = when (loadType) {
            LoadType.REFRESH -> 1
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                val lastPage = pageLocalDataSource.getPage(userName, category)
                lastPage?.nextPage ?: if (lastPage == null) 1 else return MediatorResult.Success(endOfPaginationReached = true)
            }
        }

        try {
            val remoteMovies = movieRemoteDataSource.getMovies(userName, category, apiKey, nextPage)
            val filteredRemoteMovies = remoteMovies.distinctBy { it.businessId }

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    movieLocalDataSource.deleteAll(userName, category)
                    pageLocalDataSource.deletePage(userName, category)
                }

                val startOrder = (nextPage - 1) * PAGE_SIZE
                val moviesToUpsert = filteredRemoteMovies.mapIndexed { index, remoteMovie ->
                    remoteMovie.copy(displayOrder = startOrder + index + 1)
                }
                movieLocalDataSource.insertAll(moviesToUpsert)

                val next = if (remoteMovies.isEmpty()) null else nextPage + 1
                pageLocalDataSource.insertPage(Page(userName, category, next))
            }

            return MediatorResult.Success(endOfPaginationReached = remoteMovies.isEmpty())
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }
}