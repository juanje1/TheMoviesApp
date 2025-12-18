package com.juanje.usecases

import com.juanje.data.repositories.MovieRepository
import com.juanje.domain.Movie

class LoadMovie(private val movieRepository: MovieRepository) {

    fun invokeGetMovieFavorites(userName: String) =
        movieRepository.getMovieFavorites(userName)

    fun invokeGetMovieFavorite(userName: String, movieId: Int) =
        movieRepository.getMovieFavorite(userName, movieId)

    suspend fun invokeCount(userName: String) =
        movieRepository.count(userName)

    suspend fun invokeGetAndInsertMovies(userName: String, refresh: Boolean) =
        movieRepository.getAndInsertMovies(userName, refresh)

    suspend fun invokeUpdateMovie(userName: String, movie: Movie, isFavorite: Boolean) =
        movieRepository.updateMovie(userName, movie, isFavorite)
}