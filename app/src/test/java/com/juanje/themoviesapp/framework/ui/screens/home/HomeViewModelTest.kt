package com.juanje.themoviesapp.framework.ui.screens.home

import com.juanje.themoviesapp.data.repositories.MovieRepository
import com.juanje.themoviesapp.usecases.LoadPopularMovies
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    private val apiKey = "d30e1f350220f9aad6c4110df385d380"

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Test
    fun `Listening to movies flow emits the list of movies from the server`() = runTest {
        val repository = MovieRepository(
            FakeLocalDataSource(), FakeRemoteDataSource(fakeMovies), apiKey)
        val loadPopularMovies = LoadPopularMovies(repository)

        val homeViewModel = HomeViewModel(loadPopularMovies)

        val movies = homeViewModel.state.value.movies
        Assert.assertEquals(fakeMovies, movies)
    }
}