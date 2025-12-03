package com.juanje.data.repositories

import com.juanje.data.datasources.MovieLocalDataSource
import com.juanje.data.datasources.MovieRemoteDataSource
import com.juanje.domain.Movie
import kotlinx.coroutines.flow.Flow

class MovieRepository(
    private val movieLocalDataSource: MovieLocalDataSource,
    private val movieRemoteDataSource: MovieRemoteDataSource,
    private val apiKey: String
) {
    companion object {
        const val PAGE_SIZE = 20
    }

    suspend fun getMovies(userName: String) {
        val page = getCountMovies(userName) / PAGE_SIZE + 1
        val movies = movieRemoteDataSource.getMovies(userName, apiKey, page)

        movieLocalDataSource.insertAll(movies)
    }

    suspend fun getMoviesFromDatabase(userName: String): Flow<List<Movie>> =
        movieLocalDataSource.getMovies(userName)

    suspend fun getMovieDetail(userName: String, movieId: Int) =
        movieLocalDataSource.getMovie(userName, movieId)

    private suspend fun getCountMovies(userName: String) =
        movieLocalDataSource.count(userName)

    suspend fun updateMovie(movie: Movie) =
        movieLocalDataSource.updateMovie(movie)
}