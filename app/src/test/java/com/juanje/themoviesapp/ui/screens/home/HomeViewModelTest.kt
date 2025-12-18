package com.juanje.themoviesapp.ui.screens.home

import android.content.Context
import com.juanje.data.repositories.MovieRepository
import com.juanje.themoviesapp.common.NetworkConnectivityObserver
import com.juanje.themoviesapp.ui.screens.CoroutinesTestRule
import com.juanje.themoviesapp.ui.screens.FakeMovieLocalDataSource
import com.juanje.themoviesapp.ui.screens.FakeMovieRemoteDataSource
import com.juanje.themoviesapp.ui.screens.fakeMovie
import com.juanje.themoviesapp.ui.screens.fakeMovies
import com.juanje.themoviesapp.ui.screens.fakeUserName
import com.juanje.usecases.LoadMovie
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    private val apiKey = "d30e1f350220f9aad6c4110df385d380"
    private val networkConnectivityObserver = mock<NetworkConnectivityObserver>()
    private val mainDispatcher = mock<CoroutineDispatcher>()
    private val context = mock<Context>()
    private lateinit var homeViewModel: HomeViewModel

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Before
    fun setUp() {
        val movieLocalDataSource = FakeMovieLocalDataSource()
        val movieRemoteDataSource = FakeMovieRemoteDataSource(fakeMovies)
        val movieRepository = MovieRepository(movieLocalDataSource, movieRemoteDataSource, apiKey)
        val loadMovie = LoadMovie(movieRepository)
        homeViewModel = HomeViewModel(loadMovie, networkConnectivityObserver, mainDispatcher, context)
    }

    @Test
    fun `Listening to movies flow emits the list of movies from the server`() = runTest {
        `when`(networkConnectivityObserver.isInternetAvailable(context)).thenReturn(true)

        homeViewModel.getAndInsertMovies(fakeUserName)
        val movies = homeViewModel.state.value.movies

        Assert.assertEquals(fakeMovies, movies)
    }

    @Test
    fun `Updating a movie in the local database`() = runTest {
        `when`(networkConnectivityObserver.isInternetAvailable(context)).thenReturn(true)

        homeViewModel.getAndInsertMovies(fakeUserName)
        homeViewModel.updateMovie(fakeMovie)
        homeViewModel.getAndInsertMovies(fakeUserName)
        val movies = homeViewModel.state.value.movies

        Assert.assertEquals(true, movies.find { it.id == fakeMovie.id }?.favourite)
    }
}