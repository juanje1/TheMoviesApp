package com.juanje.data.repositories

import com.juanje.data.datasources.MovieLocalDataSource
import com.juanje.data.datasources.MovieRemoteDataSource
import com.juanje.domain.Movie
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verifyBlocking
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mock

class MovieRepositoryTest {

    companion object {
        private const val userName: String = "User 1"
    }

    private val apiKey = "d30e1f350220f9aad6c4110df385d380"
    private val databaseMovies = mutableListOf<Movie>()
    private val serverMovies = mutableListOf<Movie>()
    private val lastVisible = 0
    private val sizeDatabase = 0
    private val sizeServer = 0
    private val size = 0
    private val page = 1

    @Mock
    private lateinit var movieLocalDataSource: MovieLocalDataSource
    @Mock
    private lateinit var movieRemoteDataSource: MovieRemoteDataSource
    @Mock
    private lateinit var repository: MovieRepository

    @Test
    fun `When DB is empty, server is called`() {
        initializeMocks(sizeDatabase, sizeServer+20)

        runBlocking { repository.getMovies(userName, lastVisible, size) }

        verifyBlocking(movieRemoteDataSource) { getMovies(userName, apiKey, page) }
    }

    @Test
    fun `When DB is empty, movies are saved into DB`() {
        initializeMocks(sizeDatabase, sizeServer+20)

        runBlocking { repository.getMovies(userName, lastVisible, size) }

        verifyBlocking(movieLocalDataSource) { insertAll(serverMovies) }
    }

    @Test
    fun `When DB is not empty, server is not called`() {
        initializeMocks(sizeDatabase+20, sizeServer)

        runBlocking { repository.getMovies(userName, lastVisible, size) }

        verifyBlocking(movieRemoteDataSource, times(0)) { getMovies(userName, apiKey, page) }
    }

    @Test
    fun `When DB is not empty, movies are not saved into DB`() {
        initializeMocks(sizeDatabase+20, sizeServer)

        runBlocking { repository.getMovies(userName, lastVisible, size+20) }

        verifyBlocking(movieLocalDataSource, times(0)) { insertAll(any()) }
    }

    @Test
    fun `When DB is not empty, movies are recovered from DB`() {
        initializeMocks(sizeDatabase+20, sizeServer)

        val result = runBlocking {
            repository.getMovies(userName, lastVisible, size+20).first()
        }

        assertEquals(databaseMovies, result)
    }

    private fun initializeMocks(sizeDatabase: Int, sizeServer: Int) {
        movieLocalDataSource = mock {
            onBlocking { count(userName) } doReturn sizeDatabase
            if(sizeDatabase > 0) {
                initializeDatabaseMovies(sizeDatabase)
                onBlocking { getMovies(userName) } doReturn flowOf(databaseMovies)
            }
        }

        movieRemoteDataSource = mock {
            if(sizeServer > 0) {
                initializeServerMovies(sizeServer)
                onBlocking { getMovies(userName, apiKey, page) } doReturn serverMovies
            }
        }

        repository = MovieRepository(movieLocalDataSource, movieRemoteDataSource, apiKey)
    }

    private fun initializeDatabaseMovies(sizeDatabase: Int) {
        for(i in 1..sizeDatabase) {
            databaseMovies.add(
                Movie(
                    id = i,
                    title = "Title $i",
                    overview = "Overview $i",
                    posterPath = "Poster Path $i",
                    favourite = false,
                    userName = userName
                )
            )
        }
    }

    private fun initializeServerMovies(sizeServer: Int) {
        for(i in sizeDatabase+1..sizeDatabase+sizeServer) {
            serverMovies.add(
                Movie(
                    id = i,
                    title = "Title $i",
                    overview = "Overview $i",
                    posterPath = "Poster Path $i",
                    favourite = false,
                    userName = userName
                )
            )
        }
    }
}