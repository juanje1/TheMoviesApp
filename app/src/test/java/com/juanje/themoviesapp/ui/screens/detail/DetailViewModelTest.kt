package com.juanje.themoviesapp.ui.screens.detail

import com.juanje.data.repositories.MovieRepository
import com.juanje.themoviesapp.ui.screens.common.CoroutinesTestRule
import com.juanje.themoviesapp.ui.screens.common.FakeAppIdlingResource
import com.juanje.themoviesapp.ui.screens.common.FakeFavoriteLocalDataSource
import com.juanje.themoviesapp.ui.screens.common.FakeMovieLocalDataSource
import com.juanje.themoviesapp.ui.screens.common.FakeMovieRemoteDataSource
import com.juanje.themoviesapp.ui.screens.common.fakeId
import com.juanje.themoviesapp.ui.screens.common.fakeUserName
import com.juanje.usecases.LoadMovie
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class DetailViewModelTest {
    private val apiKey = "d30e1f350220f9aad6c4110df385d380"

    private lateinit var testDispatcher: CoroutineDispatcher
    private lateinit var movieRepository: MovieRepository
    private lateinit var loadMovie: LoadMovie
    private lateinit var detailViewModel: DetailViewModel

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Before
    fun setUp() {
        testDispatcher = coroutinesTestRule.testDispatcher

        movieRepository = MovieRepository(
            movieLocalDataSource = FakeMovieLocalDataSource(),
            movieRemoteDataSource = FakeMovieRemoteDataSource(),
            favoriteLocalDataSource = FakeFavoriteLocalDataSource(),
            apiKey = apiKey
        )
        loadMovie = LoadMovie(movieRepository)
        detailViewModel = DetailViewModel(
            loadMovie = loadMovie,
            idlingResource = FakeAppIdlingResource()
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