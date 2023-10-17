package com.juanje.themoviesapp.framework.data.server

import com.juanje.themoviesapp.data.datasources.RemoteDataSource
import com.juanje.themoviesapp.domain.Movie
import com.juanje.themoviesapp.framework.data.server.dataclasses.toMovie

class MovieServerDataSource(private val movieService: MovieService): RemoteDataSource {

    override suspend fun getMovies(apiKey: String): List<Movie> =
        movieService.getMovies(apiKey)
            .results
            .map { it.toMovie() }
}