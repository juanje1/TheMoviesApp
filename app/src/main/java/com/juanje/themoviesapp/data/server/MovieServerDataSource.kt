package com.juanje.themoviesapp.data.server

import com.juanje.data.datasources.RemoteDataSource
import com.juanje.domain.Movie
import com.juanje.themoviesapp.data.server.dataclasses.toMovie

class MovieServerDataSource(private val movieService: MovieService): RemoteDataSource {

    override suspend fun getMovies(apiKey: String, page: Int): List<Movie> =
        movieService.getMovies(apiKey, page)
            .results
            .map { it.toMovie() }
}