package com.juanje.themoviesapp.framework.data.database

import com.juanje.themoviesapp.data.datasources.LocalDataSource
import com.juanje.themoviesapp.domain.Movie
import com.juanje.themoviesapp.framework.data.database.daos.MovieDao
import com.juanje.themoviesapp.framework.data.database.entities.toMovie
import com.juanje.themoviesapp.framework.data.database.entities.toMovieEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MovieDatabaseDataSource(private val dao: MovieDao): LocalDataSource {

    override suspend fun getMovies(): Flow<List<Movie>> {
        return dao.getMovies().map { movies ->
            movies.map { it.toMovie() } }
    }

    override suspend fun insertAll(movies: List<Movie>) {
        dao.insertAll(movies.map { it.toMovieEntity() })
    }

    override suspend fun updateMovie(movie: Movie) {
        dao.updateMovie(movie.toMovieEntity())
    }

    override suspend fun count(): Int {
        return dao.count()
    }
}