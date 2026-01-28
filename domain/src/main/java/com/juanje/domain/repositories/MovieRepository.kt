package com.juanje.domain.repositories

import com.juanje.domain.dataclasses.Movie
import com.juanje.domain.dataclasses.MovieFavorite
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getMovieFavorites(userName: String): Flow<List<MovieFavorite>>
    fun getMovieFavorite(userName: String, movieId: Int): Flow<MovieFavorite>
    suspend fun count(userName: String): Int
    suspend fun getAndInsertMovies(userName: String, refresh: Boolean = false)
    suspend fun updateMovieFavorite(userName: String, movie: Movie, isFavorite: Boolean)
}