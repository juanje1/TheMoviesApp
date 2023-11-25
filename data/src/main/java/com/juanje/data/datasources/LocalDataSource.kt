package com.juanje.data.datasources

import com.juanje.domain.Movie
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    suspend fun getMovies(): Flow<List<Movie>>
    suspend fun insertAll(movies: List<Movie>)
    suspend fun updateMovie(movie: Movie)
    suspend fun count(): Int
}