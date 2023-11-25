package com.juanje.data.datasources

import com.juanje.domain.Movie

interface RemoteDataSource {
    suspend fun getMovies(apiKey: String, page: Int): List<Movie>
}