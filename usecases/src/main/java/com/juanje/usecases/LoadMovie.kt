package com.juanje.usecases

import com.juanje.data.repositories.MovieRepository
import com.juanje.domain.Movie

class LoadMovie(private val movieRepository: MovieRepository) {

    suspend fun invokeGetMovies(userName: String, connectivity: Boolean) =
        movieRepository.getMovies(userName, connectivity)

    suspend fun invokeGetMovieDetail(userName: String, movieId: Int) =
        movieRepository.getMovieDetail(userName, movieId)

    suspend fun invokeUpdateMovie(movie: Movie) =
        movieRepository.updateMovie(movie)
}