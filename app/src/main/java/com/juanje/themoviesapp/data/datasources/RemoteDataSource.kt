package com.juanje.themoviesapp.data.datasources

import com.juanje.themoviesapp.domain.Movie

interface RemoteDataSource {
    suspend fun getMovies(apiKey: String): List<Movie>
}