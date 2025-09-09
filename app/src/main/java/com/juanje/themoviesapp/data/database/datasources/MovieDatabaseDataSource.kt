package com.juanje.themoviesapp.data.database.datasources

import com.juanje.data.datasources.MovieLocalDataSource
import com.juanje.domain.Movie
import com.juanje.themoviesapp.data.database.daos.MovieDao
import com.juanje.themoviesapp.data.database.dataclasses.toMovie
import com.juanje.themoviesapp.data.database.dataclasses.toMovieDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MovieDatabaseDataSource(private val movieDao: MovieDao): MovieLocalDataSource {

    override suspend fun getMovies(userName: String): Flow<List<Movie>> =
        movieDao.getMovies(userName).map { movies -> movies.map { it.toMovie() } }

    override suspend fun getMovie(userName: String, movieId: Int): Movie =
        movieDao.getMovie(userName, movieId).toMovie()

    override suspend fun count(userName: String): Int =
        movieDao.count(userName)

    override suspend fun insertAll(movies: List<Movie>) =
        movieDao.insertAll(movies.map { it.toMovieDatabase() })

    override suspend fun updateMovie(movie: Movie) =
        movieDao.updateMovie(movie.toMovieDatabase())
}