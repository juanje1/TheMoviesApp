package com.juanje.themoviesapp.data.server.services

import com.juanje.themoviesapp.data.server.dataclasses.MovieServer
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {
    @GET("discover/movie")
    suspend fun getMovies(
        @Query("api_key") apikey: String,
        @Query("page") page: Int
    ): MovieServer
}