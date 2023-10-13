package com.juanje.themoviesapp.framework.data.server

import com.juanje.themoviesapp.framework.data.server.dataclasses.MovieResult
import retrofit2.http.GET

interface MovieService {
    @GET("discover/movie?api_key=d30e1f350220f9aad6c4110df385d380")
    suspend fun getMovies(): MovieResult
}