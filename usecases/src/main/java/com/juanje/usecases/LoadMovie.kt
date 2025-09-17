package com.juanje.usecases

import com.juanje.data.repositories.MovieRepository
import com.juanje.domain.Movie

class LoadMovie(private val movieRepository: MovieRepository) {

    suspend fun invokeGetMovies(userName: String, lastVisible: Int, size: Int) =
        movieRepository.getMovies(userName, lastVisible, size)

    suspend fun invokeGetMovieDetail(userName: String, movieId: Int) =
        movieRepository.getMovieDetail(userName, movieId)

    suspend fun invokeGetCountMovies(userName: String) =
        movieRepository.getCountMovies(userName)

    suspend fun invokeUpdateMovie(movie: Movie) =
        movieRepository.updateMovie(movie)
}