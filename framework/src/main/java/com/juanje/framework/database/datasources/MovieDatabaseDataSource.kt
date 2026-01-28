package com.juanje.framework.database.datasources

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

class MovieDatabaseDataSource @Inject constructor(
    private val movieDao: MovieDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
): MovieLocalDataSource {

    override fun getMovies(userName: String): Flow<List<Movie>> =
        movieDao.getMovies(userName).map { movies -> movies.map { it.toMovie() } }

    override fun getMovie(userName: String, movieId: Int): Flow<Movie> =
        movieDao.getMovie(userName, movieId).map { it.toMovie() }

    override suspend fun count(userName: String): Int =
        withContext(ioDispatcher) {
            movieDao.count(userName)
        }

    override suspend fun deleteAll(userName: String) =
        withContext(ioDispatcher) {
            movieDao.deleteAll(userName)
        }

    override suspend fun insertAll(movies: List<Movie>) =
        withContext(ioDispatcher) {
            movieDao.insertAll(movies.map { it.toMovieDatabase() })
        }

    override suspend fun refreshMoviesTx(userName: String, movies: List<Movie>) =
        withContext(ioDispatcher) {
            movieDao.refreshMoviesTx(userName, movies.map { it.toMovieDatabase() })
        }
}