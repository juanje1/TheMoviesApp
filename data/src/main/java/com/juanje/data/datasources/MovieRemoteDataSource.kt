package com.juanje.data.datasources

import com.juanje.domain.dataclasses.Movie

interface MovieRemoteDataSource {
    suspend fun getMovies(userName: String, category: String, apiKey: String, page: Int): List<Movie>
}