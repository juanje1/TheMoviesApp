package com.juanje.framework.server.datasources

import com.juanje.data.datasources.MovieRemoteDataSource
import com.juanje.domain.dataclasses.Movie
import com.juanje.framework.server.services.MovieService
import com.juanje.framework.server.dataclasses.toMovie
import javax.inject.Inject

class MovieServerDataSource @Inject constructor(
    private val movieService: MovieService
): MovieRemoteDataSource {

    override suspend fun getMovies(userName: String, apiKey: String, page: Int): List<Movie> =
        movieService.getMovies(apiKey, page).results.map { it.toMovie(userName) }
}