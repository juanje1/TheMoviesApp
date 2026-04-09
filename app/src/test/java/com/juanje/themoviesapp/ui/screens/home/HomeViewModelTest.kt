package com.juanje.themoviesapp.ui.screens.home

import androidx.lifecycle.SavedStateHandle
import androidx.paging.ExperimentalPagingApi
import androidx.paging.testing.asSnapshot
import com.juanje.data.interfaces.MovieMapper
import com.juanje.data.interfaces.MovieRemoteMediatorProvider
import com.juanje.data.repositories.MovieRepositoryImpl
import com.juanje.domain.MovieFactory.FAKE_USER_NAME
import com.juanje.domain.MovieFactory.fakeMoviesList
import com.juanje.domain.dataclasses.MovieFavorite
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
    private lateinit var favoriteLocalDataSource: FakeFavoriteLocalDataSource
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

        favoriteLocalDataSource = FakeFavoriteLocalDataSource()
        movieLocalDataSource = FakeMovieLocalDataSource(favoriteLocalDataSource)

        runTest(coroutinesTestRule.testDispatcher) {
            movieLocalDataSource.insertAll(fakeMoviesList)
        }

        movieRepository = MovieRepositoryImpl(
            movieLocalDataSource = movieLocalDataSource,
            mediatorProvider = mediatorProvider,
            favoriteLocalDataSource = favoriteLocalDataSource,
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
        whenever(movieMapper.map(any())).thenAnswer { it.getArgument<MovieFavorite>(0) }

        // When
        advanceUntilIdle()
        val movieFavoriteList = homeViewModel.movies.asSnapshot()

        // Then
        assertEquals(fakeMoviesList, movieFavoriteList.map { it.movie })
        movieFavoriteList.forEach { assertFalse(it.isFavorite) }
    }

    @Test
    fun `Updating the movies in the local database`() = runTest {
        // Given
        whenever(movieMapper.map(any())).thenAnswer { it.getArgument<MovieFavorite>(0) }

        // When
        advanceUntilIdle()
        fakeMoviesList.forEach { movie -> homeViewModel.updateMovie(movie, true) }
        
        advanceUntilIdle()
        val movieFavoriteList = homeViewModel.movies.asSnapshot()

        // Then
        assertEquals(fakeMoviesList, movieFavoriteList.map { it.movie })
        movieFavoriteList.forEach { assertTrue(it.isFavorite) }
    }
}