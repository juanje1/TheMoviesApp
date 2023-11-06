package com.juanje.themoviesapp.framework.data.server

import com.juanje.themoviesapp.data.datasources.RemoteDataSource
import com.juanje.themoviesapp.domain.Movie
import com.juanje.themoviesapp.framework.data.server.dataclasses.toMovie

class MovieServerDataSource(private val movieService: MovieService): RemoteDataSource {

    override suspend fun getMovies(apiKey: String, page: Int): List<Movie> =
        movieService.getMovies(apiKey, page)
            .results
            .map { it.toMovie() }
}