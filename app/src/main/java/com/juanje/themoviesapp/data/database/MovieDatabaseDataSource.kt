package com.juanje.themoviesapp.data.database

import com.juanje.data.datasources.LocalDataSource
import com.juanje.domain.Movie
import com.juanje.themoviesapp.data.database.daos.MovieDao
import com.juanje.themoviesapp.data.database.entities.toMovie
import com.juanje.themoviesapp.data.database.entities.toMovieEntity
import kotlinx.coroutines.flow.map

class MovieDatabaseDataSource(private val dao: MovieDao): LocalDataSource {

    override suspend fun getMovies() =
        dao.getMovies().map { movies -> movies.map { it.toMovie() } }

    override suspend fun insertAll(movies: List<Movie>) =
        dao.insertAll(movies.map { it.toMovieEntity() })

    override suspend fun updateMovie(movie: Movie) =
        dao.updateMovie(movie.toMovieEntity())

    override suspend fun count() =
        dao.count()
}