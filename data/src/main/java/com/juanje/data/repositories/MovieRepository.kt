package com.juanje.data.repositories

import com.juanje.data.datasources.LocalDataSource
import com.juanje.data.datasources.RemoteDataSource
import com.juanje.domain.Movie
import kotlinx.coroutines.flow.Flow

class MovieRepository(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val apiKey: String
) {
    companion object {
        const val PAGE_THRESHOLD = 10
        const val PAGE_SIZE = 20
    }

    suspend fun getMovies(lastVisible: Int, size: Int): Flow<List<Movie>> {
        if(lastVisible >= size - PAGE_THRESHOLD) {
            val page = getCountMovies() / PAGE_SIZE + 1
            localDataSource.insertAll(remoteDataSource.getMovies(apiKey, page))
        }

        return localDataSource.getMovies()
    }

    suspend fun getMoviesDetail(): Flow<List<Movie>> = localDataSource.getMovies()

    suspend fun getCountMovies(): Int = localDataSource.count()

    suspend fun updateMovie(movie: Movie) = localDataSource.updateMovie(movie)
}