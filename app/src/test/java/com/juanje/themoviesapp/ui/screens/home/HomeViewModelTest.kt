package com.juanje.themoviesapp.ui.screens.home

import com.juanje.data.repositories.MovieRepository
import com.juanje.usecases.LoadMovie
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    private val apiKey = "d30e1f350220f9aad6c4110df385d380"

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Test
    fun `Listening to movies flow emits the list of movies from the server`() = runTest {
        val repository = MovieRepository(
            FakeMovieLocalDataSource(),
            FakeMovieRemoteDataSource(fakeMovies),
            apiKey
        )
        val loadMovie = LoadMovie(repository)

        val homeViewModel = HomeViewModel(loadMovie)

        val movies = homeViewModel.state.value.movies
        Assert.assertEquals(fakeMovies, movies)
    }
}