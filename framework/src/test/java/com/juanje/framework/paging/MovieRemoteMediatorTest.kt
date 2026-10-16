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
import com.juanje.domain.MovieFactory.FAKE_USER_NAME
import com.juanje.domain.MovieFactory.createFakeMovies
import com.juanje.domain.dataclasses.Movie
import com.juanje.domain.dataclasses.Page
import com.juanje.framework.database.TheMoviesAppDatabase
import com.juanje.framework.database.dataclasses.MovieDatabase
import com.juanje.framework.database.dataclasses.toMovieDatabase
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
import org.mockito.Mockito.never
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
class MovieRemoteMediatorTest {
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
    fun `When DB is empty and append, Mediator should request page 1`() = runTest {
        // Given
        val newMovies = createFakeMovies()

        whenever(pageLocalDataSource.getPage(any(), any())).thenReturn(null)
        whenever(movieRemoteDataSource.getMovies(any(), any(), eq(FAKE_API_KEY), eq(1))).thenReturn(newMovies)

        val pagingState = PagingState<Int, MovieDatabase>(
            pages = listOf(),
            anchorPosition = null,
            config = PagingConfig(pageSize = 20),
            leadingPlaceholderCount = 0
        )

        // When
        val result = movieRemoteMediator.load(LoadType.APPEND, pagingState)

        // Then
        verify(pageLocalDataSource).getPage(any(), any())
        verify(movieRemoteDataSource).getMovies(any(), any(), eq(FAKE_API_KEY), eq(1))
        verify(movieLocalDataSource).insertAll(any())
        verify(pageLocalDataSource).insertPage(any())

        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @Test
    fun `When DB is not empty and refresh, Mediator should request page 1`() = runTest {
        // Given
        val lastMoviesDatabase = createFakeMovies().map { it.toMovieDatabase() }
        val newMovies = createFakeMovies()

        whenever(movieRemoteDataSource.getMovies(any(), any(), eq(FAKE_API_KEY), eq(1))).thenReturn(newMovies)

        val pagingState = PagingState(
            pages = listOf(PagingSource.LoadResult.Page(data = lastMoviesDatabase, prevKey = null, nextKey = 2)),
            anchorPosition = 20,
            config = PagingConfig(pageSize = 20),
            leadingPlaceholderCount = 0
        )

        // When
        val result = movieRemoteMediator.load(LoadType.REFRESH, pagingState)

        // Then
        verify(movieRemoteDataSource).getMovies(any(), any(), eq(FAKE_API_KEY), eq(1))
        verify(movieLocalDataSource).deleteAll(any(), any())
        verify(pageLocalDataSource).deletePage(any(), any())
        verify(movieLocalDataSource).insertAll(any())
        verify(pageLocalDataSource).insertPage(any())

        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @Test
    fun `When DB is not empty and append, request next page`() = runTest {
        // Given
        val lastMoviesDatabase = createFakeMovies().map { it.toMovieDatabase() }
        val newMovies = createFakeMovies(init = 21)

        whenever(pageLocalDataSource.getPage(any(), any())).thenReturn(Page(FAKE_USER_NAME, FAKE_CATEGORY, nextPage = 2))
        whenever(movieRemoteDataSource.getMovies(any(), any(), eq(FAKE_API_KEY), eq(2))).thenReturn(newMovies)

        val pagingState = PagingState(
            pages = listOf(PagingSource.LoadResult.Page(data = lastMoviesDatabase, prevKey = null, nextKey = 2)),
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
        verify(pageLocalDataSource).insertPage(any())

        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @Test
    fun `When DB is not empty and prepend, endOfPaginationReached is true`() = runTest {
        // Given
        val lastMoviesDatabase = createFakeMovies().map { it.toMovieDatabase() }

        val pagingState = PagingState(
            pages = listOf(PagingSource.LoadResult.Page(data = lastMoviesDatabase, prevKey = null, nextKey = 2)),
            anchorPosition = 20,
            config = PagingConfig(pageSize = 20),
            leadingPlaceholderCount = 0
        )

        // When
        val result = movieRemoteMediator.load(LoadType.PREPEND, pagingState)

        // Then
        verify(movieRemoteDataSource, never()).getMovies(any(), any(), any(), any())
        verify(movieLocalDataSource, never()).insertAll(any())
        verify(pageLocalDataSource, never()).insertPage(any())

        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @Test
    fun `Mediator should assign correct displayOrder based on current count`() = runTest {
        // Given
        val lastMoviesDatabase = createFakeMovies(quantity = 40).map { it.toMovieDatabase() }
        val movies = createFakeMovies(init = 41)

        whenever(pageLocalDataSource.getPage(any(), any())).thenReturn(Page(FAKE_USER_NAME, FAKE_CATEGORY, nextPage = 3))
        whenever(movieRemoteDataSource.getMovies(any(), any(), eq(FAKE_API_KEY), eq(3))).thenReturn(movies)

        val pagingState = PagingState(
            pages = listOf(PagingSource.LoadResult.Page(data = lastMoviesDatabase, prevKey = 1, nextKey = 3)),
            anchorPosition = 20,
            config = PagingConfig(pageSize = 20),
            leadingPlaceholderCount = 0
        )

        // When
        val result = movieRemoteMediator.load(LoadType.APPEND, pagingState)

        // Then
        val captor = argumentCaptor<List<Movie>>()

        verify(pageLocalDataSource).getPage(any(), any())
        verify(movieRemoteDataSource).getMovies(any(), any(), eq(FAKE_API_KEY), eq(3))
        verify(movieLocalDataSource).insertAll(captor.capture())
        verify(pageLocalDataSource).insertPage(any())

        assertEquals(41, captor.firstValue[0].displayOrder)
        assertEquals(42, captor.firstValue[1].displayOrder)
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @Test
    fun `When UnknownHostException is thrown in getMovies, the exception is retrieved`() = runTest {
        // Given
        whenever(pageLocalDataSource.getPage(any(), any())).thenReturn(null)
        whenever(movieRemoteDataSource.getMovies(any(), any(), eq(FAKE_API_KEY), eq(1))).thenAnswer { throw UnknownHostException() }

        val pagingState = PagingState<Int, MovieDatabase>(
            pages = listOf(),
            anchorPosition = null,
            config = PagingConfig(pageSize = 20),
            leadingPlaceholderCount = 0
        )

        // When
        val result = movieRemoteMediator.load(LoadType.APPEND, pagingState)

        // Then
        verify(pageLocalDataSource).getPage(any(), any())
        verify(movieRemoteDataSource).getMovies(any(), any(), eq(FAKE_API_KEY), eq(1))
        verify(movieLocalDataSource, never()).insertAll(any())
        verify(pageLocalDataSource, never()).insertPage(any())

        assertTrue(result is RemoteMediator.MediatorResult.Error)
        assertTrue((result as RemoteMediator.MediatorResult.Error).throwable is UnknownHostException)
    }

    @Test
    fun `When SQLiteException is thrown in insertAll, the exception is retrieved`() = runTest {
        // Given
        val newMovies = createFakeMovies()

        whenever(pageLocalDataSource.getPage(any(), any())).thenReturn(null)
        whenever(movieRemoteDataSource.getMovies(any(), any(), eq(FAKE_API_KEY), eq(1))).thenReturn(newMovies)
        whenever(movieLocalDataSource.insertAll(any())).thenAnswer { throw SQLiteException() }

        val pagingState = PagingState<Int, MovieDatabase>(
            pages = listOf(),
            anchorPosition = null,
            config = PagingConfig(pageSize = 20),
            leadingPlaceholderCount = 0
        )

        // When
        val result = movieRemoteMediator.load(LoadType.APPEND, pagingState)

        // Then
        verify(pageLocalDataSource).getPage(any(), any())
        verify(movieRemoteDataSource).getMovies(any(), any(), eq(FAKE_API_KEY), eq(1))
        verify(movieLocalDataSource).insertAll(any())
        verify(pageLocalDataSource, never()).insertPage(any())

        assertTrue(result is RemoteMediator.MediatorResult.Error)
        assertTrue((result as RemoteMediator.MediatorResult.Error).throwable is SQLiteException)
    }
}