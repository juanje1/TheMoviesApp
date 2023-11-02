package com.juanje.themoviesapp.usecases

import com.juanje.themoviesapp.data.repositories.MovieRepository
import com.juanje.themoviesapp.domain.Movie
import kotlinx.coroutines.flow.Flow

class LoadPopularMovies(private val repository: MovieRepository) {

    suspend fun invokeGetMovies(): Flow<List<Movie>> = repository.getMovies()

    suspend fun invokeGetMoviesDetail(): Flow<List<Movie>> = repository.getMoviesDetail()

    suspend fun invokeUpdateMovie(movie: Movie) = repository.updateMovie(movie)
}