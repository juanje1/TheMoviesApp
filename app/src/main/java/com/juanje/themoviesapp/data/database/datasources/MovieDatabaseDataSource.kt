package com.juanje.themoviesapp.data.database.datasources

import com.juanje.data.datasources.MovieLocalDataSource
import com.juanje.domain.Movie
import com.juanje.themoviesapp.data.IoDispatcher
import com.juanje.themoviesapp.data.database.daos.MovieDao
import com.juanje.themoviesapp.data.database.dataclasses.toMovie
import com.juanje.themoviesapp.data.database.dataclasses.toMovieDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class MovieDatabaseDataSource(
    private val movieDao: MovieDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
): MovieLocalDataSource {

    override suspend fun getMovies(userName: String): Flow<List<Movie>> =
        withContext(ioDispatcher) {
            movieDao.getMovies(userName).map { movies -> movies.map { it.toMovie() } }
        }

    override suspend fun getMovie(userName: String, movieId: Int): Movie =
        withContext(ioDispatcher) {
            movieDao.getMovie(userName, movieId).toMovie()
        }

    override suspend fun count(userName: String): Int =
        withContext(ioDispatcher) {
            movieDao.count(userName)
        }

    override suspend fun insertAll(movies: List<Movie>) =
        withContext(ioDispatcher) {
            movieDao.insertAll(movies.map { it.toMovieDatabase() })
        }

    override suspend fun updateMovie(movie: Movie) =
        withContext(ioDispatcher) {
            movieDao.updateMovie(movie.toMovieDatabase())
        }
}