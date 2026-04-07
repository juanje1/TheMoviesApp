package com.juanje.data.repositories

import android.database.sqlite.SQLiteException
import com.juanje.data.datasources.FavoriteLocalDataSource
import com.juanje.data.datasources.MovieLocalDataSource
import com.juanje.domain.MovieFactory.FAKE_CATEGORY
import com.juanje.domain.MovieFactory.FAKE_ID_FAVORITE
import com.juanje.domain.MovieFactory.FAKE_USER_NAME
import com.juanje.domain.MovieFactory.createFakeMovies
import com.juanje.domain.MovieFactory.generateBusinessId
import com.juanje.domain.common.AppError
import com.juanje.domain.dataclasses.Favorite
import com.juanje.domain.dataclasses.MovieFavorite
import com.juanje.data.interfaces.MovieMapper
import com.juanje.data.interfaces.MovieRemoteMediatorProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertFailsWith

@RunWith(MockitoJUnitRunner::class)
class MovieRepositoryTest {
    @Mock private lateinit var movieLocalDataSource: MovieLocalDataSource
    @Mock private lateinit var mediatorProvider: MovieRemoteMediatorProvider
    @Mock private lateinit var favoriteLocalDataSource: FavoriteLocalDataSource
    @Mock private lateinit var movieMapper: MovieMapper<Any, MovieFavorite>

    private lateinit var movieRepository: MovieRepositoryImpl

    @Before
    fun setup() {
        movieRepository = MovieRepositoryImpl(
            movieLocalDataSource = movieLocalDataSource,
            mediatorProvider = mediatorProvider,
            favoriteLocalDataSource = favoriteLocalDataSource,
            movieMapper = movieMapper
        )
    }

    @Test
    fun `When getMovie is called, should combine movie and favorite status correctly`() = runTest {
        // Given
        val movie = createFakeMovies()[FAKE_ID_FAVORITE - 1]
        val businessId = generateBusinessId(FAKE_ID_FAVORITE)
        val isFavorite = true

        whenever(movieLocalDataSource.getMovie(any(), any(), any())).thenReturn(flowOf(movie))
        whenever(favoriteLocalDataSource.getFavorite(movie.businessId)).thenReturn(flowOf(isFavorite))

        // When
        val result = movieRepository.getMovie(businessId, FAKE_USER_NAME, FAKE_CATEGORY).first()

        // Then
        verify(movieLocalDataSource).getMovie(businessId, FAKE_USER_NAME, FAKE_CATEGORY)
        verify(favoriteLocalDataSource).getFavorite(movie.businessId)
        assertEquals(movie, result.movie)
        assertEquals(isFavorite, result.isFavorite)
    }

    @Test
    fun `When isFavorite is true, must insert favorite with generated ID`() = runTest {
        // Given
        val moviesList = createFakeMovies()
        val movieToFavorite = moviesList[FAKE_ID_FAVORITE - 1]
        val businessId = generateBusinessId(FAKE_ID_FAVORITE)

        // When
        movieRepository.updateMovie(movieToFavorite, true)

        // Then
        val captor = argumentCaptor<Favorite>()

        verify(favoriteLocalDataSource).insertFavorite(captor.capture())
        verify(favoriteLocalDataSource, never()).deleteFavorite(any())
        assertEquals(businessId, captor.firstValue.businessId)
    }

    @Test
    fun `When isFavorite is false, must delete favorite`() = runTest {
        // Given
        val moviesList = createFakeMovies()
        val movieToFavorite = moviesList[FAKE_ID_FAVORITE - 1]
        val businessId = generateBusinessId(FAKE_ID_FAVORITE)

        // When
        movieRepository.updateMovie(movieToFavorite, false)

        // Then
        verify(favoriteLocalDataSource).deleteFavorite(businessId)
        verify(favoriteLocalDataSource, never()).insertFavorite(any())
    }

    @Test
    fun `When SQLiteException is thrown in updateMovie, evolve with AppError Database`() = runTest {
        // Given
        val moviesList = createFakeMovies()
        val movieToFavorite = moviesList[FAKE_ID_FAVORITE - 1]
        whenever(favoriteLocalDataSource.deleteFavorite(any())).thenThrow(SQLiteException())

        // When & Then
        assertFailsWith<AppError.Database> {
            movieRepository.updateMovie(movieToFavorite, false)
        }
    }
}