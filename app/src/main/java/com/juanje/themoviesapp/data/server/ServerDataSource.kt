package com.juanje.themoviesapp.data.server

import com.juanje.themoviesapp.data.Movie
import com.juanje.themoviesapp.data.server.dataclasses.toMovie
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ServerDataSource {

    suspend fun getMovies(): List<Movie> {
        return Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MovieService::class.java)
            .getMovies()
            .results
            .map { it.toMovie() }
    }
}