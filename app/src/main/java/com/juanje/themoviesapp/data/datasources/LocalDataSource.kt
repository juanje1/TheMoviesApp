package com.juanje.themoviesapp.data.datasources

import com.juanje.themoviesapp.domain.Movie
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    suspend fun getMovies(): Flow<List<Movie>>
    suspend fun insertAll(movies: List<Movie>)
    suspend fun updateMovie(movie: Movie)
    suspend fun count(): Int
}