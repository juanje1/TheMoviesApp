package com.juanje.usecases

import com.juanje.data.repositories.MovieRepository
import com.juanje.domain.Movie
import kotlinx.coroutines.flow.Flow

class LoadPopularMovies(private val repository: MovieRepository) {

    suspend fun invokeGetMovies(lastVisible: Int, size: Int): Flow<List<Movie>> =
        repository.getMovies(lastVisible, size)

    suspend fun invokeGetMoviesDetail(): Flow<List<Movie>> =
        repository.getMoviesDetail()

    suspend fun invokeGetCountMovies(): Int =
        repository.getCountMovies()

    suspend fun invokeUpdateMovie(movie: Movie) =
        repository.updateMovie(movie)
}