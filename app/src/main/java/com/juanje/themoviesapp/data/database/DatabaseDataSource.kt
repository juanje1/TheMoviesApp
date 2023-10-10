package com.juanje.themoviesapp.data.database

import com.juanje.themoviesapp.data.Movie
import com.juanje.themoviesapp.data.database.daos.MovieDao
import com.juanje.themoviesapp.data.database.entities.toMovie
import com.juanje.themoviesapp.data.database.entities.toMovieEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DatabaseDataSource(private val dao: MovieDao) {

    val movies: Flow<List<Movie>> = dao.getMovies().map { movies ->
        movies.map { it.toMovie() } }

    suspend fun insertAll(movies: List<Movie>) {
        dao.insertAll(movies.map { it.toMovieEntity() })
    }

    suspend fun updateMovie(movie: Movie) {
        dao.updateMovie(movie.toMovieEntity())
    }

    suspend fun count(): Int {
        return dao.count()
    }
}