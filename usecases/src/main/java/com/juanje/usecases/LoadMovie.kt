package com.juanje.usecases

import com.juanje.data.repositories.MovieRepository
import com.juanje.domain.Movie
import kotlinx.coroutines.flow.Flow

class LoadMovie(private val movieRepository: MovieRepository) {

    suspend fun invokeGetMovies(userName: String, lastVisible: Int, size: Int): Flow<List<Movie>> =
        movieRepository.getMovies(userName, lastVisible, size)

    suspend fun invokeGetMoviesDetail(userName: String): Flow<List<Movie>> =
        movieRepository.getMoviesDetail(userName)

    suspend fun invokeGetCountMovies(userName: String): Int =
        movieRepository.getCountMovies(userName)

    suspend fun invokeUpdateMovie(movie: Movie) =
        movieRepository.updateMovie(movie)
}