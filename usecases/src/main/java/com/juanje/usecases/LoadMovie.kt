package com.juanje.usecases

import com.juanje.domain.dataclasses.Movie
import com.juanje.domain.repositories.MovieRepository
import javax.inject.Inject

class LoadMovie @Inject constructor(
    private val movieRepository: MovieRepository
) {

    fun invokeGetMovieFavorites(userName: String) =
        movieRepository.getMovieFavorites(userName)

    fun invokeGetMovieFavorite(userName: String, movieId: Int) =
        movieRepository.getMovieFavorite(userName, movieId)

    suspend fun invokeCount(userName: String) =
        movieRepository.count(userName)

    suspend fun invokeGetAndInsertMovies(userName: String, refresh: Boolean) =
        movieRepository.getAndInsertMovies(userName, refresh)

    suspend fun invokeUpdateMovieFavorite(userName: String, movie: Movie, isFavorite: Boolean) =
        movieRepository.updateMovieFavorite(userName, movie, isFavorite)
}