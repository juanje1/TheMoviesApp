package com.juanje.themoviesapp.ui.screens.detail

import com.juanje.data.repositories.MovieRepositoryImpl
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
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class DetailViewModelTest {
    private val apiKey = "d30e1f350220f9aad6c4110df385d380"

    private lateinit var movieRepository: MovieRepositoryImpl
    private lateinit var loadMovie: LoadMovie
    private lateinit var detailViewModel: DetailViewModel

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Before
    fun setUp() {
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
            mainDispatcher = coroutinesTestRule.testDispatcher
        )
    }

    @Test
    fun `Listening to movie detail from the database`() = runTest {
        // When
        detailViewModel.setArgsFlow(fakeUserName, fakeId)
        advanceUntilIdle()

        // Then
        Assert.assertEquals(fakeId, detailViewModel.state.value.movie?.movie?.id)
        Assert.assertEquals(fakeUserName, detailViewModel.state.value.movie?.movie?.userName)
    }
}