package com.juanje.themoviesapp.data

import com.juanje.themoviesapp.data.database.DatabaseDataSource
import com.juanje.themoviesapp.data.server.ServerDataSource
import kotlinx.coroutines.flow.Flow

class MovieRepository(
    private val databaseDataSource: DatabaseDataSource,
    private val serverDataSource: ServerDataSource
) {
    val movies: Flow<List<Movie>> = databaseDataSource.movies

    suspend fun updateMovie(movie: Movie) {
        databaseDataSource.updateMovie(movie)
    }

    suspend fun requestMovies() {
        val isDbEmpty = databaseDataSource.count() == 0
        if (isDbEmpty) {
            databaseDataSource.insertAll(serverDataSource.getMovies())
        }
    }
}