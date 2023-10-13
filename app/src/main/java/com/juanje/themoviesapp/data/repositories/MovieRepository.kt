package com.juanje.themoviesapp.data.repositories

import com.juanje.themoviesapp.data.datasources.LocalDataSource
import com.juanje.themoviesapp.data.datasources.RemoteDataSource
import com.juanje.themoviesapp.domain.Movie
import kotlinx.coroutines.flow.Flow

class MovieRepository(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) {

    suspend fun getPopularMovies(): Flow<List<Movie>> {
        val isDbEmpty = localDataSource.count() == 0
        if (isDbEmpty) {
            localDataSource.insertAll(remoteDataSource.getMovies())
        }
        return localDataSource.getMovies()
    }

    suspend fun updateMovie(movie: Movie) {
        localDataSource.updateMovie(movie)
    }
}