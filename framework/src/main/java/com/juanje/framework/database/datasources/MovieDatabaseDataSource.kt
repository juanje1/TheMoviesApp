package com.juanje.framework.database.datasources

import androidx.paging.PagingSource
import com.juanje.data.datasources.MovieLocalDataSource
import com.juanje.domain.IoDispatcher
import com.juanje.domain.dataclasses.Movie
import com.juanje.framework.database.daos.MovieDao
import com.juanje.framework.database.dataclasses.toMovie
import com.juanje.framework.database.dataclasses.toMovieDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class MovieDatabaseDataSource @Inject constructor(
    private val movieDao: MovieDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
): MovieLocalDataSource {

    override fun <T : Any> getMovies(userName: String, category: String): PagingSource<Int, T> =
        movieDao.getMovies(userName, category) as PagingSource<Int, T>

    override fun getMovie(businessId: String, userName: String, category: String): Flow<Movie> =
        movieDao.getMovie(businessId, userName, category).map { it.toMovie() }

    override suspend fun deleteAll(userName: String, category: String) =
        withContext(ioDispatcher) {
            movieDao.deleteAll(userName, category)
        }

    override suspend fun insertAll(movies: List<Movie>) =
        withContext(ioDispatcher) {
            movieDao.insertAll(movies.map { it.toMovieDatabase() })
        }
}