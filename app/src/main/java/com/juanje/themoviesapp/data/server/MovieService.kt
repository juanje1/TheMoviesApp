package com.juanje.themoviesapp.data.server

import com.juanje.themoviesapp.data.server.dataclasses.MovieResult
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {
    @GET("discover/movie")
    suspend fun getMovies(
        @Query("api_key") apikey: String,
        @Query("page") page: Int
    ): MovieResult
}