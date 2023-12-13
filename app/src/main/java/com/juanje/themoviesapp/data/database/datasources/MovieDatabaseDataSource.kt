package com.juanje.themoviesapp.data.database.datasources

import com.juanje.data.datasources.MovieLocalDataSource
import com.juanje.domain.Movie
import com.juanje.themoviesapp.data.database.daos.MovieDao
import com.juanje.themoviesapp.data.database.entities.toMovie
import com.juanje.themoviesapp.data.database.entities.toMovieEntity
import kotlinx.coroutines.flow.map

class MovieDatabaseDataSource(private val movieDao: MovieDao): MovieLocalDataSource {

    override suspend fun getMovies(userName: String) =
        movieDao.getMovies(userName).map { movies -> movies.map { it.toMovie() } }

    override suspend fun count(userName: String) =
        movieDao.count(userName)

    override suspend fun insertAll(movies: List<Movie>) =
        movieDao.insertAll(movies.map { it.toMovieEntity() })

    override suspend fun updateMovie(movie: Movie) =
        movieDao.updateMovie(movie.toMovieEntity())
}