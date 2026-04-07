package com.juanje.themoviesapp.ui.screens.home

import androidx.lifecycle.SavedStateHandle
import androidx.paging.ExperimentalPagingApi
import androidx.paging.testing.asSnapshot
import com.juanje.data.repositories.MovieRepositoryImpl
import com.juanje.domain.MovieFactory.FAKE_USER_NAME
import com.juanje.domain.dataclasses.Movie
import com.juanje.domain.dataclasses.MovieFavorite
import com.juanje.data.interfaces.MovieMapper
import com.juanje.data.interfaces.MovieRemoteMediatorProvider
import com.juanje.domain.MovieFactory.createFakeMovie
import com.juanje.domain.MovieFactory.fakeMovieWithFavoritesList
import com.juanje.domain.MovieFactory.fakeMovieWithoutFavoritesList
import com.juanje.themoviesapp.common.network.NetworkConnectivityObserver
import com.juanje.themoviesapp.ui.navigation.Screen
import com.juanje.themoviesapp.ui.screens.common.CoroutinesTestRule
import com.juanje.themoviesapp.ui.screens.common.FakeAppIdlingResource
import com.juanje.themoviesapp.ui.screens.common.FakeFavoriteLocalDataSource
import com.juanje.themoviesapp.ui.screens.common.FakeMovieLocalDataSource
import com.juanje.usecases.LoadMovie
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalPagingApi::class, ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class HomeViewModelTest {
    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Mock private lateinit var mediatorProvider: MovieRemoteMediatorProvider
    @Mock private lateinit var movieMapper: MovieMapper<Any, MovieFavorite>
    @Mock private lateinit var networkConnectivityObserver: NetworkConnectivityObserver

    private lateinit var movieLocalDataSource: FakeMovieLocalDataSource
    private lateinit var movieRepository: MovieRepositoryImpl
    private lateinit var loadMovie: LoadMovie
    private lateinit var homeViewModel: HomeViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        val savedStateHandle = SavedStateHandle(mapOf(
            Screen.Home::userName.name to FAKE_USER_NAME
        ))

        whenever(mediatorProvider.getMediator<Any>(anyString(), anyString())).thenReturn(null)
        whenever(networkConnectivityObserver.observe()).thenReturn(flowOf(true))

        movieLocalDataSource = FakeMovieLocalDataSource()

        movieRepository = MovieRepositoryImpl(
            movieLocalDataSource = movieLocalDataSource,
            mediatorProvider = mediatorProvider,
            favoriteLocalDataSource = FakeFavoriteLocalDataSource(),
            movieMapper = movieMapper
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
        // Given
        val fakeMovies = fakeMovieWithoutFavoritesList.map { it.movie }
        movieLocalDataSource.insertAll(fakeMovies)

        whenever(movieMapper.map(any())).thenAnswer { inv ->
            val movieIn = inv.arguments[0] as Movie
            fakeMovieWithoutFavoritesList.first { it.movie.businessId == movieIn.businessId }
        }

        // When
        advanceUntilIdle()
        val movieFavoriteList = homeViewModel.movies.asSnapshot()

        // Then
        assertEquals(fakeMovieWithoutFavoritesList, movieFavoriteList)
        movieFavoriteList.forEachIndexed { i, movieFavorite ->
            assertEquals(fakeMovies[i].businessId, movieFavorite.movie.businessId)
            assertFalse(movieFavorite.isFavorite)
        }
    }

    @Test
    fun `Updating the movies in the local database`() = runTest {
        // Given
        val movieList = (1..2).map { createFakeMovie(it, it) }
        movieLocalDataSource.insertAll(movieList)

        whenever(movieMapper.map(any())).thenAnswer { invocation ->
            val input = invocation.getArgument<Movie>(0)
            val originalMovie = fakeMovieWithFavoritesList.find { it.movie.businessId == input.businessId }
            originalMovie?.copy(isFavorite = true) ?: throw Exception("No found")
        }

        // When
        advanceUntilIdle()
        movieList.forEach { movie -> homeViewModel.updateMovie(movie, true) }
        advanceUntilIdle()
        val movieFavoriteList = homeViewModel.movies.asSnapshot()

        // Then
        assertEquals(movieList, movieFavoriteList.map { it.movie })
        movieFavoriteList.forEach { assertTrue(it.isFavorite) }
    }
}