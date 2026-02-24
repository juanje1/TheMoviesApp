package com.juanje.themoviesapp.ui.screens.home

import androidx.lifecycle.SavedStateHandle
import com.juanje.data.repositories.MovieRepositoryImpl
import com.juanje.themoviesapp.common.NetworkConnectivityObserver
import com.juanje.themoviesapp.ui.navigation.Screen
import com.juanje.themoviesapp.ui.screens.common.CoroutinesTestRule
import com.juanje.themoviesapp.ui.screens.common.FakeAppIdlingResource
import com.juanje.themoviesapp.ui.screens.common.FakeFavoriteLocalDataSource
import com.juanje.themoviesapp.ui.screens.common.FakeMovieLocalDataSource
import com.juanje.themoviesapp.ui.screens.common.FakeMovieRemoteDataSource
import com.juanje.themoviesapp.ui.screens.common.createFakeMovie
import com.juanje.themoviesapp.ui.screens.common.fakeFavorites
import com.juanje.themoviesapp.ui.screens.common.fakeFavoritesId
import com.juanje.themoviesapp.ui.screens.common.fakeMovieFavorite
import com.juanje.themoviesapp.ui.screens.common.fakeMovieFavoriteNoFavorite
import com.juanje.themoviesapp.ui.screens.common.fakeUserName
import com.juanje.usecases.LoadMovie
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class HomeViewModelTest {
    private val apiKey = "d30e1f350220f9aad6c4110df385d380"
    private lateinit var favoriteLocalDataSource: FakeFavoriteLocalDataSource
    private lateinit var movieRepository: MovieRepositoryImpl
    private lateinit var loadMovie: LoadMovie
    private lateinit var homeViewModel: HomeViewModel

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Mock
    private lateinit var networkConnectivityObserver: NetworkConnectivityObserver

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        val savedStateHandle = SavedStateHandle(mapOf(
            Screen.Home::userName.name to fakeUserName
        ))

        whenever(networkConnectivityObserver.observe()).thenReturn(flowOf(true))
        favoriteLocalDataSource = FakeFavoriteLocalDataSource()

        movieRepository = MovieRepositoryImpl(
            movieLocalDataSource = FakeMovieLocalDataSource(),
            movieRemoteDataSource = FakeMovieRemoteDataSource(),
            favoriteLocalDataSource = favoriteLocalDataSource,
            apiKey = apiKey
        )
        loadMovie = LoadMovie(movieRepository)
        homeViewModel = HomeViewModel(
            loadMovie = loadMovie,
            idlingResource = FakeAppIdlingResource(),
            connectivityObserver = networkConnectivityObserver,
            mainDispatcher = coroutinesTestRule.testDispatcher,
            savedStateHandle = savedStateHandle
        )
    }

    @Test
    fun `Listening to movies flow emits the list of movies from the server without favorites`() = runTest {
        // When
        advanceUntilIdle()

        // Then
        Assert.assertEquals(fakeMovieFavoriteNoFavorite, homeViewModel.state.value.movies)
    }

    @Test
    fun `Updating a movie in the local database`() = runTest {
        // Given
        val movieFavoriteList = fakeFavoritesId.map { createFakeMovie(it, it) }

        // When
        advanceUntilIdle()

        movieFavoriteList.map { homeViewModel.updateMovie(it) }
        advanceUntilIdle()

        // Then
        Assert.assertEquals(fakeMovieFavorite, homeViewModel.state.value.movies)
        fakeFavorites.map { assertTrue(favoriteLocalDataSource.isFavorite(it.businessId)) }
    }
}