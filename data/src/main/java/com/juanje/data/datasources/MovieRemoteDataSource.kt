package com.juanje.data.datasources

import com.juanje.domain.Movie

interface MovieRemoteDataSource {

    suspend fun getMovies(userName: String, apiKey: String, page: Int): List<Movie>
}