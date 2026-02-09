package com.juanje.data.repositories

import android.database.sqlite.SQLiteException
import com.juanje.data.datasources.FavoriteLocalDataSource
import com.juanje.data.datasources.MovieLocalDataSource
import com.juanje.data.datasources.MovieRemoteDataSource
import com.juanje.domain.common.AppError
import com.juanje.domain.dataclasses.Favorite
import com.juanje.domain.dataclasses.Movie
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.eq
import org.mockito.kotlin.never
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.net.UnknownHostException
import kotlin.test.assertFailsWith

@RunWith(MockitoJUnitRunner::class)
class MovieRepositoryTest {
    companion object {
        private const val USERNAME: String = "User 1"
    }

    private val apiKey = "d30e1f350220f9aad6c4110df385d380"

    private lateinit var movieRepository: MovieRepositoryImpl

    @Mock
    private lateinit var movieLocalDataSource: MovieLocalDataSource

    @Mock
    private lateinit var movieRemoteDataSource: MovieRemoteDataSource

    @Mock
    private lateinit var favoriteLocalDataSource: FavoriteLocalDataSource

    @Before
    fun setup() {
        movieRepository = MovieRepositoryImpl(
            movieLocalDataSource = movieLocalDataSource,
            movieRemoteDataSource = movieRemoteDataSource,
            favoriteLocalDataSource = favoriteLocalDataSource,
            apiKey = apiKey
        )
    }

    @Test
    fun `When DB is empty, request the page 1`() = runTest {
        // Given
        whenever(movieLocalDataSource.count(USERNAME)).thenReturn(0)
        whenever(movieRemoteDataSource.getMovies(USERNAME, apiKey, 1)).thenReturn(createMovies())
        whenever(movieLocalDataSource.insertAll(any())).thenReturn(Unit)

        // When
        movieRepository.getAndInsertMovies(USERNAME)

        // Then
        verify(movieRemoteDataSource).getMovies(USERNAME, apiKey, 1)
    }

    @Test
    fun `When DB is not empty with 20 movies, request the page 2`() = runTest {
        // Given
        whenever(movieLocalDataSource.count(USERNAME)).thenReturn(20)
        whenever(movieRemoteDataSource.getMovies(USERNAME, apiKey, 2)).thenReturn(createMovies(init = 20))
        whenever(movieLocalDataSource.insertAll(any())).thenReturn(Unit)

        // When
        movieRepository.getAndInsertMovies(USERNAME)

        // Then
        verify(movieRemoteDataSource).getMovies(USERNAME, apiKey, 2)
    }

    @Test
    fun `When refresh is true, call to refreshMoviesTx and request page 1`() = runTest {
        // Given
        whenever(movieRemoteDataSource.getMovies(USERNAME, apiKey, 1)).thenReturn(createMovies())
        whenever(movieLocalDataSource.refreshMoviesTx(eq(USERNAME), any())).thenReturn(Unit)

        // When
        movieRepository.getAndInsertMovies(USERNAME, true)

        // Then
        verify(movieLocalDataSource).refreshMoviesTx(eq(USERNAME), any())
        verify(movieLocalDataSource, times(0)).count(any())
        verify(movieLocalDataSource, times(0)).insertAll(any())
    }

    @Test
    fun `When refresh is false, call to insertAll`() = runTest {
        // Given
        whenever(movieLocalDataSource.count(USERNAME)).thenReturn(0)
        whenever(movieRemoteDataSource.getMovies(any(), any(), any())).thenReturn(createMovies())
        whenever(movieLocalDataSource.insertAll(any())).thenReturn(Unit)

        // When
        movieRepository.getAndInsertMovies(USERNAME)

        // Then
        verify(movieLocalDataSource).insertAll(any())
        verify(movieLocalDataSource, times(0)).refreshMoviesTx(eq(USERNAME), any())
    }

    @Test
    fun `When count is not 0, displayOrder is get correctly`() = runTest {
        // Given
        val count = 40
        val newMovies = createMovies(init = 40)

        whenever(movieLocalDataSource.count(USERNAME)).thenReturn(count)
        whenever(movieRemoteDataSource.getMovies(any(), any(), any())).thenReturn(newMovies)

        // When
        movieRepository.getAndInsertMovies(USERNAME)

        // Then
        val captorList = argumentCaptor<List<Movie>>()
        verify(movieLocalDataSource).insertAll(captorList.capture())

        assertEquals(41, captorList.firstValue[0].displayOrder)
        assertEquals(42, captorList.firstValue[1].displayOrder)
    }

    @Test
    fun `When UnknownHostException is thrown, getAndInsertMoves evolve with AppError Network`() = runTest {
        // Given
        whenever(movieLocalDataSource.count(USERNAME)).thenReturn(0)
        doAnswer { throw UnknownHostException() }
            .whenever(movieRemoteDataSource).getMovies(any(), any(), any())

        // When & Then
        assertFailsWith<AppError.Network> {
            movieRepository.getAndInsertMovies(USERNAME)
        }

        verify(movieLocalDataSource, never()).insertAll(any())
    }

    @Test
    fun `When SQLiteException is thrown, getAndInsertMoves evolve with AppError Database`() = runTest {
        // Given
        doAnswer { throw SQLiteException() }
            .whenever(movieLocalDataSource).count(USERNAME)

        // When & Then
        assertFailsWith<AppError.Database> {
            movieRepository.getAndInsertMovies(USERNAME)
        }
    }

    @Test
    fun `When isFavorite is true, must insert favorite with generated ID`() = runTest {
        // Given
        val moviesList = createMovies()
        val movieToFavorite = moviesList[5]
        val businessId = createFavorite(movieToFavorite).businessId

        // When
        movieRepository.updateMovieFavorite(USERNAME, movieToFavorite, true)

        // Then
        val captor = argumentCaptor<Favorite>()

        verify(favoriteLocalDataSource).insertFavorite(captor.capture())
        assertEquals(businessId, captor.firstValue.businessId)
        verify(favoriteLocalDataSource, never()).deleteFavorite(any())
    }

    @Test
    fun `When isFavorite is false, must delete favorite`() = runTest {
        // Given
        val moviesList = createMovies()
        val movieToFavorite = moviesList[5]
        val businessId = createFavorite(movieToFavorite).businessId

        // When
        movieRepository.updateMovieFavorite(USERNAME, movieToFavorite, false)

        // Then
        verify(favoriteLocalDataSource).deleteFavorite(businessId)
        verify(favoriteLocalDataSource, never()).insertFavorite(any())
    }

    @Test
    fun `When SQLiteException is thrown, updateMovieFavorite evolve with AppError Database`() = runTest {
        // Given
        val moviesList = createMovies()
        val movieToFavorite = moviesList[5]

        doAnswer { throw SQLiteException() }
            .whenever(favoriteLocalDataSource).deleteFavorite(any())

        // When & Then
        assertFailsWith<AppError.Database> {
            movieRepository.updateMovieFavorite(USERNAME, movieToFavorite, false)
        }
    }

    private fun createMovies(init: Int = 0, quantity: Int = 20): List<Movie> {
        val movies = mutableListOf<Movie>()

        for(i in init + 1..init + quantity) {
            movies.add(
                Movie(
                    id = i,
                    title = "Title $i",
                    overview = "Overview $i",
                    posterPath = "Poster Path $i",
                    releaseDate = "Release Date $i",
                    userName = USERNAME,
                    displayOrder = i
                )
            )
        }

        return movies
    }

    private fun createFavorite(movie: Movie) =
        Favorite(businessId = "$USERNAME|${movie.title}|${movie.releaseDate}")
}