package com.juanje.framework.paging

import android.content.Context
import android.database.sqlite.SQLiteException
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.juanje.data.datasources.MovieLocalDataSource
import com.juanje.data.datasources.MovieRemoteDataSource
import com.juanje.data.datasources.PageLocalDataSource
import com.juanje.domain.MovieFactory.FAKE_API_KEY
import com.juanje.domain.MovieFactory.FAKE_CATEGORY
import com.juanje.domain.MovieFactory.FAKE_LAST_ID_PAGE_1
import com.juanje.domain.MovieFactory.FAKE_LAST_ID_PAGE_2
import com.juanje.domain.MovieFactory.FAKE_USER_NAME
import com.juanje.domain.MovieFactory.createFakeMovies
import com.juanje.domain.dataclasses.Movie
import com.juanje.domain.dataclasses.Page
import com.juanje.framework.database.TheMoviesAppDatabase
import com.juanje.framework.database.dataclasses.MovieDatabase
import com.juanje.framework.database.paging.MovieRemoteMediator
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner
import java.net.UnknownHostException

@OptIn(ExperimentalPagingApi::class)
@RunWith(RobolectricTestRunner::class)
class MovieRepositoryTest {
    @Mock private lateinit var movieLocalDataSource: MovieLocalDataSource
    @Mock private lateinit var movieRemoteDataSource: MovieRemoteDataSource
    @Mock private lateinit var pageLocalDataSource: PageLocalDataSource

    private lateinit var db: TheMoviesAppDatabase
    private lateinit var movieRemoteMediator: MovieRemoteMediator

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        val context = ApplicationProvider.getApplicationContext<Context>()

        db = Room.inMemoryDatabaseBuilder(context, TheMoviesAppDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        movieRemoteMediator = MovieRemoteMediator(
            db = db,
            movieLocalDataSource = movieLocalDataSource,
            movieRemoteDataSource = movieRemoteDataSource,
            pageLocalDataSource = pageLocalDataSource,
            userName = FAKE_USER_NAME,
            category = FAKE_CATEGORY,
            apiKey = FAKE_API_KEY
        )
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun `When DB is empty, Mediator should request page 1`() = runTest {
        // Given
        val movies = createFakeMovies()

        whenever(movieRemoteDataSource.getMovies(any(), any(), eq(FAKE_API_KEY), eq(1)))
            .thenReturn(movies)

        val pagingState = PagingState<Int, MovieDatabase>(
            pages = listOf(),
            anchorPosition = null,
            config = PagingConfig(pageSize = 20),
            leadingPlaceholderCount = 0
        )

        // When
        val result = movieRemoteMediator.load(LoadType.REFRESH, pagingState)

        // Then
        verify(movieRemoteDataSource).getMovies(any(), any(), eq(FAKE_API_KEY), eq(1))
        verify(movieLocalDataSource).insertAll(any())
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @Test
    fun `When loading more and last movie exists, request next page`() = runTest {
        // Given
        val lastMovie = createFakeMovies()[FAKE_LAST_ID_PAGE_1]
        val lastMovieDatabase = lastMovie.toMovieDatabase()
        val movies = createFakeMovies(init = 20)

        whenever(pageLocalDataSource.getPage(any(), any()))
            .thenReturn(Page(FAKE_USER_NAME, FAKE_CATEGORY, nextPage = 2))
        whenever(movieRemoteDataSource.getMovies(any(), any(), eq(FAKE_API_KEY), eq(2)))
            .thenReturn(movies)

        val pagingState = PagingState(
            pages = listOf(PagingSource.LoadResult.Page(data = listOf(lastMovieDatabase), prevKey = null, nextKey = 2)),
            anchorPosition = 20,
            config = PagingConfig(pageSize = 20),
            leadingPlaceholderCount = 0
        )

        // When
        val result = movieRemoteMediator.load(LoadType.APPEND, pagingState)

        // Then
        verify(pageLocalDataSource).getPage(any(), any())
        verify(movieRemoteDataSource).getMovies(any(), any(), eq(FAKE_API_KEY), eq(2))
        verify(movieLocalDataSource).insertAll(any())
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @Test
    fun `When LoadType is refresh, database should be cleared and repopulated`() = runTest {
        // Given
        val lastMovie = createFakeMovies()[FAKE_LAST_ID_PAGE_1]
        val lastMovieDatabase = lastMovie.toMovieDatabase()
        val movies = createFakeMovies()

        whenever(movieRemoteDataSource.getMovies(any(), any(), eq(FAKE_API_KEY), eq(1)))
            .thenReturn(movies)

        val pagingState = PagingState(
            pages = listOf(PagingSource.LoadResult.Page(data = listOf(lastMovieDatabase), prevKey = null, nextKey = 2)),
            anchorPosition = 20,
            config = PagingConfig(pageSize = 20),
            leadingPlaceholderCount = 0
        )

        // When
        val result = movieRemoteMediator.load(LoadType.REFRESH, pagingState)

        // Then
        verify(movieLocalDataSource).deleteAll(any(), any())
        verify(movieLocalDataSource).insertAll(any())
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @Test
    fun `Mediator should assign correct displayOrder based on current count`() = runTest {
        // Given
        val lastMovie = createFakeMovies(quantity = 40)[FAKE_LAST_ID_PAGE_2]
        val lastMovieDatabase = lastMovie.toMovieDatabase()
        val movies = createFakeMovies(init = 40)

        whenever(pageLocalDataSource.getPage(any(), any()))
            .thenReturn(Page(FAKE_USER_NAME, FAKE_CATEGORY, nextPage = 3))
        whenever(movieRemoteDataSource.getMovies(any(), any(), eq(FAKE_API_KEY), eq(3)))
            .thenReturn(movies)

        val pagingState = PagingState(
            pages = listOf(PagingSource.LoadResult.Page(data = listOf(lastMovieDatabase), prevKey = 1, nextKey = 3)),
            anchorPosition = 20,
            config = PagingConfig(pageSize = 20),
            leadingPlaceholderCount = 0
        )

        // When
        val result = movieRemoteMediator.load(LoadType.APPEND, pagingState)

        // Then
        val captor = argumentCaptor<List<Movie>>()

        verify(movieLocalDataSource).insertAll(captor.capture())
        assertEquals(41, captor.firstValue[0].displayOrder)
        assertEquals(42, captor.firstValue[1].displayOrder)
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @Test
    fun `When UnknownHostException is thrown in getMovies, the exception is retrieved`() = runTest {
        // Given
        whenever(movieRemoteDataSource.getMovies(any(), any(), any(), any()))
            .thenAnswer { throw UnknownHostException() }

        val pagingState = PagingState<Int, MovieDatabase>(
            pages = listOf(),
            anchorPosition = null,
            config = PagingConfig(pageSize = 20),
            leadingPlaceholderCount = 0
        )

        // When
        val result = movieRemoteMediator.load(LoadType.REFRESH, pagingState)

        // Then
        assertTrue(result is RemoteMediator.MediatorResult.Error)
        assertTrue((result as RemoteMediator.MediatorResult.Error).throwable is UnknownHostException)
    }

    @Test
    fun `When SQLiteException is thrown in insertAll, the exception is retrieved`() = runTest {
        // Given
        val movies = createFakeMovies()

        whenever(movieRemoteDataSource.getMovies(any(), any(), any(), any()))
            .thenReturn(movies)
        whenever(movieLocalDataSource.insertAll(any()))
            .thenAnswer { throw SQLiteException() }

        val pagingState = PagingState<Int, MovieDatabase>(
            pages = listOf(),
            anchorPosition = null,
            config = PagingConfig(pageSize = 20),
            leadingPlaceholderCount = 0
        )

        // When
        val result = movieRemoteMediator.load(LoadType.REFRESH, pagingState)

        // Then
        assertTrue(result is RemoteMediator.MediatorResult.Error)
        assertTrue((result as RemoteMediator.MediatorResult.Error).throwable is SQLiteException)
    }

    private fun Movie.toMovieDatabase() = MovieDatabase(
        businessId = businessId,
        title = title,
        overview = overview,
        posterPath = posterPath,
        releaseDate = releaseDate,
        userName = userName,
        category = category,
        displayOrder = displayOrder
    )
}