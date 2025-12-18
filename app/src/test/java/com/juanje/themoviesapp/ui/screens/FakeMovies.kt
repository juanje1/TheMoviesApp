package com.juanje.themoviesapp.ui.screens

import com.juanje.data.datasources.MovieLocalDataSource
import com.juanje.data.datasources.MovieRemoteDataSource
import com.juanje.domain.Movie
import kotlinx.coroutines.flow.flowOf

const val fakeUserName = "User 1"
const val fakeId = 1

val fakeMovies = listOf(
    Movie(1, "Title 1", "Overview 1", "Poster Path 1", false, fakeUserName),
    Movie(2, "Title 2", "Overview 2", "Poster Path 2", false, fakeUserName),
    Movie(3, "Title 3", "Overview 3", "Poster Path 3", false, fakeUserName),
    Movie(4, "Title 4", "Overview 4", "Poster Path 4", false, fakeUserName)
)
val fakeMovie = Movie(1, "Title 1", "Overview 1", "Poster Path 1", false, "User 1")

class FakeMovieLocalDataSource : MovieLocalDataSource {

    private var movies = mutableListOf<Movie>()
    private var movie = fakeMovie

    override suspend fun getMoviesForUserFlow(userName: String) = flowOf(movies)

    override suspend fun getMovie(userName: String, movieId: Int): Movie = movie

    override suspend fun count(userName: String): Int = movies.size

    override suspend fun insertAll(movies: List<Movie>) {
        this.movies += movies
    }

    override suspend fun upsertAll(movie: Movie) {
        this.movies = this.movies.map { if (it.id == movie.id) movie else it }.toMutableList()
    }
}

class FakeMovieRemoteDataSource(private val movies: List<Movie> = emptyList()) : MovieRemoteDataSource {

    override suspend fun getMovies(userName: String, apiKey: String, page: Int) = movies
}