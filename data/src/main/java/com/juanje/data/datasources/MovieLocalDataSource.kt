package com.juanje.data.datasources

import com.juanje.domain.Movie
import kotlinx.coroutines.flow.Flow

interface MovieLocalDataSource {

    suspend fun getMovies(userName: String): Flow<List<Movie>>
    suspend fun getMovie(userName: String, movieId: Int): Movie
    suspend fun count(userName: String): Int
    suspend fun insertAll(movies: List<Movie>)
    suspend fun updateMovie(movie: Movie)
}