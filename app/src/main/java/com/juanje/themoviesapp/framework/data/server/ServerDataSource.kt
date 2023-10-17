package com.juanje.themoviesapp.framework.data.server

import com.juanje.themoviesapp.data.datasources.RemoteDataSource
import com.juanje.themoviesapp.domain.Movie
import com.juanje.themoviesapp.framework.data.server.dataclasses.toMovie

class ServerDataSource(private val movieService: MovieService): RemoteDataSource {

    override suspend fun getMovies(apiKey: String): List<Movie> {
        val movieResult = movieService.getMovies(apiKey)

        return movieResult.results.map { it.toMovie() }
    }
}