package com.juanje.themoviesapp.ui.screens.detail

import androidx.lifecycle.SavedStateHandle
import com.juanje.data.repositories.MovieRepositoryImpl
import com.juanje.themoviesapp.ui.navigation.Screen
import com.juanje.themoviesapp.ui.screens.common.CoroutinesTestRule
import com.juanje.themoviesapp.ui.screens.common.FakeAppIdlingResource
import com.juanje.themoviesapp.ui.screens.common.FakeFavoriteLocalDataSource
import com.juanje.themoviesapp.ui.screens.common.FakeMovieLocalDataSource
import com.juanje.themoviesapp.ui.screens.common.FakeMovieRemoteDataSource
import com.juanje.themoviesapp.ui.screens.common.fakeId
import com.juanje.themoviesapp.ui.screens.common.fakeUserName
import com.juanje.usecases.LoadMovie
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class DetailViewModelTest {
    private val apiKey = "d30e1f350220f9aad6c4110df385d380"

    private lateinit var movieRepository: MovieRepositoryImpl
    private lateinit var loadMovie: LoadMovie
    private lateinit var detailViewModel: DetailViewModel

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        val savedStateHandle = SavedStateHandle(mapOf(
            Screen.Detail::userName.name to fakeUserName,
            Screen.Detail::movieId.name to fakeId
        ))

        movieRepository = MovieRepositoryImpl(
            movieLocalDataSource = FakeMovieLocalDataSource(),
            movieRemoteDataSource = FakeMovieRemoteDataSource(),
            favoriteLocalDataSource = FakeFavoriteLocalDataSource(),
            apiKey = apiKey
        )
        loadMovie = LoadMovie(movieRepository)
        detailViewModel = DetailViewModel(
            loadMovie = loadMovie,
            idlingResource = FakeAppIdlingResource(),
            mainDispatcher = coroutinesTestRule.testDispatcher,
            savedStateHandle = savedStateHandle
        )
    }

    @Test
    fun `Listening to movie detail from the database`() = runTest {
        // When
        advanceUntilIdle()

        // Then
        Assert.assertEquals(fakeId, detailViewModel.state.value.movie?.movie?.id)
        Assert.assertEquals(fakeUserName, detailViewModel.state.value.movie?.movie?.userName)
    }
}