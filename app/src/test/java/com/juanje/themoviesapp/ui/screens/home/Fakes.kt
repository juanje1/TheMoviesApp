package com.juanje.themoviesapp.ui.screens.home

import com.juanje.data.datasources.MovieLocalDataSource
import com.juanje.data.datasources.MovieRemoteDataSource
import com.juanje.domain.Movie
import kotlinx.coroutines.flow.flowOf

val fakeMovies = listOf(
    Movie(1, "Title 1", "Overview 1", "Poster Path 1", false),
    Movie(2, "Title 2", "Overview 2", "Poster Path 2", false),
    Movie(3, "Title 3", "Overview 3", "Poster Path 3", false),
    Movie(4, "Title 4", "Overview 4", "Poster Path 4", false)
)

class FakeMovieLocalDataSource : MovieLocalDataSource {

    private var movies = mutableListOf<Movie>()

    override suspend fun getMovies() = flowOf(movies)

    override suspend fun insertAll(movies: List<Movie>) {
        this.movies += movies
    }

    override suspend fun updateMovie(movie: Movie) {
        this.movies = this.movies.map { if (it.id == movie.id) movie else it }.toMutableList()
    }

    override suspend fun count(): Int = movies.size
}

class FakeMovieRemoteDataSource(private val movies: List<Movie> = emptyList()) : MovieRemoteDataSource {

    override suspend fun getMovies(apiKey: String, page: Int) = movies

}