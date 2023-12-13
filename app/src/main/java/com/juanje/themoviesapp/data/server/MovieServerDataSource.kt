package com.juanje.themoviesapp.data.server

import com.juanje.data.datasources.MovieRemoteDataSource
import com.juanje.domain.Movie
import com.juanje.themoviesapp.data.server.dataclasses.toMovie

class MovieServerDataSource(private val movieService: MovieService): MovieRemoteDataSource {

    override suspend fun getMovies(userName: String, apiKey: String, page: Int): List<Movie> =
        movieService.getMovies(apiKey, page)
            .results
            .map { it.toMovie(userName) }
}