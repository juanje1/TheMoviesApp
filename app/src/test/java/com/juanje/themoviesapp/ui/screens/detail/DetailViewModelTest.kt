package com.juanje.themoviesapp.ui.screens.detail

import androidx.lifecycle.SavedStateHandle
import androidx.paging.ExperimentalPagingApi
import com.juanje.data.repositories.MovieRepositoryImpl
import com.juanje.domain.MovieFactory.FAKE_CATEGORY
import com.juanje.domain.MovieFactory.FAKE_ID_DETAIL
import com.juanje.domain.MovieFactory.FAKE_USER_NAME
import com.juanje.domain.dataclasses.MovieFavorite
import com.juanje.domain.interfaces.Mapper
import com.juanje.domain.interfaces.MovieRemoteMediatorProvider
import com.juanje.themoviesapp.ui.navigation.Screen
import com.juanje.themoviesapp.ui.screens.common.CoroutinesTestRule
import com.juanje.themoviesapp.ui.screens.common.FakeAppIdlingResource
import com.juanje.themoviesapp.ui.screens.common.FakeFavoriteLocalDataSource
import com.juanje.themoviesapp.ui.screens.common.FakeMovieLocalDataSource
import com.juanje.themoviesapp.ui.screens.common.fakeMoviesList
import com.juanje.themoviesapp.ui.screens.common.generateBusinessId
import com.juanje.usecases.LoadMovie
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertNotNull

@OptIn(ExperimentalPagingApi::class, ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class DetailViewModelTest {
    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Mock private lateinit var mediatorProvider: MovieRemoteMediatorProvider
    @Mock private lateinit var mapper: Mapper<Any, MovieFavorite>

    private lateinit var movieRepository: MovieRepositoryImpl
    private lateinit var loadMovie: LoadMovie
    private lateinit var detailViewModel: DetailViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        val savedStateHandle = SavedStateHandle(mapOf(
            Screen.Detail::businessId.name to generateBusinessId(FAKE_ID_DETAIL),
            Screen.Detail::userName.name to FAKE_USER_NAME,
            Screen.Detail::category.name to FAKE_CATEGORY,
        ))

        whenever(mediatorProvider.getMediator<Any>(anyString(), anyString())).thenReturn(null)

        runTest {
            val fakeMovieLocalDataSource = FakeMovieLocalDataSource()
            fakeMovieLocalDataSource.insertAll(fakeMoviesList)

            movieRepository = MovieRepositoryImpl(
                movieLocalDataSource = fakeMovieLocalDataSource,
                mediatorProvider = mediatorProvider,
                favoriteLocalDataSource = FakeFavoriteLocalDataSource(),
                mapper = mapper
            )
            loadMovie = LoadMovie(movieRepository)
            detailViewModel = DetailViewModel(
                loadMovie = loadMovie,
                idlingResource = FakeAppIdlingResource(),
                mainDispatcher = coroutinesTestRule.testDispatcher,
                savedStateHandle = savedStateHandle
            )
        }
    }

    @Test
    fun `Listening to movie detail from the database`() = runTest {
        // Given
        val fakeMovie = fakeMoviesList[FAKE_ID_DETAIL - 1]

        // When
        advanceUntilIdle()

        // Then
        val movie = detailViewModel.state.value.movieFavorite?.movie

        assertEquals(fakeMovie, movie)
        assertNotNull(detailViewModel.state.value.movieFavorite)
    }

    @Test
    fun `When movie is not found, state should show error`() = runTest {
        // Given
        val nonExistentId = generateBusinessId(999)
        val savedStateHandle = SavedStateHandle(mapOf(
            Screen.Detail::businessId.name to nonExistentId,
            Screen.Detail::userName.name to FAKE_USER_NAME,
            Screen.Detail::category.name to FAKE_CATEGORY,
        ))
        detailViewModel = DetailViewModel(
            loadMovie = loadMovie,
            idlingResource = FakeAppIdlingResource(),
            mainDispatcher = coroutinesTestRule.testDispatcher,
            savedStateHandle = savedStateHandle
        )

        // When
        advanceUntilIdle()

        // Then
        assertNull(detailViewModel.state.value.movieFavorite)
        assertNotNull(detailViewModel.state.value.error)
    }

    @Test
    fun `ViewModel should load the correct movie from the SavedStateHandle ID`() = runTest {
        // Given
        val fakeBusinessId = generateBusinessId(FAKE_ID_DETAIL)

        // When
        advanceUntilIdle()

        // Then
        assertEquals(FAKE_USER_NAME, detailViewModel.state.value.movieFavorite?.movie?.userName)
        assertEquals(fakeBusinessId, detailViewModel.state.value.movieFavorite?.movie?.businessId)
    }
}