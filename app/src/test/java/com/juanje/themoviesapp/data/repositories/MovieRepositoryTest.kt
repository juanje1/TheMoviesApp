package com.juanje.themoviesapp.data.repositories

import com.juanje.themoviesapp.domain.Movie
import com.juanje.themoviesapp.framework.data.database.MovieDatabaseDataSource
import com.juanje.themoviesapp.framework.data.server.MovieServerDataSource
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mock

class MovieRepositoryTest {

    private val apiKey = "d30e1f350220f9aad6c4110df385d380"
    private val databaseMovies = listOf(Movie(
        1, "Title 1", "Overview 1", "Poster Path 1", false)
    )
    private val serverMovies = listOf(Movie(
        2, "Title 2", "Overview 2", "Poster Path 2", false)
    )

    @Mock
    lateinit var movieDatabaseDataSource: MovieDatabaseDataSource
    @Mock
    private lateinit var movieServerDataSource: MovieServerDataSource
    @Mock
    private lateinit var repository: MovieRepository

    @Test
    fun `When DB is empty, server is called`() {
        initializeMocks(0)

        runBlocking { repository.getMovies() }

        verifyBlocking(movieServerDataSource) { getMovies(apiKey) }
    }

    @Test
    fun `When DB is empty, movies are saved into DB`() {
        initializeMocks(0)

        runBlocking { repository.getMovies() }

        verifyBlocking(movieDatabaseDataSource) { insertAll(serverMovies) }
    }

    @Test
    fun `When DB is not empty, server is not called`() {
        initializeMocks(1)

        runBlocking { repository.getMovies() }

        verifyBlocking(movieServerDataSource, times(0)) { getMovies(apiKey) }
    }

    @Test
    fun `When DB is not empty, movies are not saved into DB`() {
        initializeMocks(1)

        runBlocking { repository.getMovies() }

        verifyBlocking(movieDatabaseDataSource, times(0)) { insertAll(any()) }
    }

    @Test
    fun `When DB is not empty, movies are recovered from DB`() {
        initializeMocks(1)

        val result = runBlocking {
            repository.getMovies().first()
        }

        assertEquals(databaseMovies, result)
    }

    private fun initializeMocks(databaseEmpty: Int) {
        movieDatabaseDataSource = mock {
            onBlocking { count() } doReturn databaseEmpty
            if (databaseEmpty == 1) {
                onBlocking { getMovies() } doReturn flowOf(databaseMovies)
            }
        }
        movieServerDataSource = mock {
            onBlocking { getMovies(apiKey) } doReturn serverMovies
        }
        repository = MovieRepository(movieDatabaseDataSource, movieServerDataSource, apiKey)
    }
}