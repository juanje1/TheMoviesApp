package com.juanje.data.datasources

import com.juanje.domain.dataclasses.Movie
import kotlinx.coroutines.flow.Flow

interface MovieLocalDataSource {
    fun getMovies(userName: String): Flow<List<Movie>>
    fun getMovie(userName: String, movieId: Int): Flow<Movie>
    suspend fun count(userName: String): Int
    suspend fun deleteAll(userName: String)
    suspend fun insertAll(movies: List<Movie>)
    suspend fun refreshMoviesTx(userName: String, movies: List<Movie>)
}