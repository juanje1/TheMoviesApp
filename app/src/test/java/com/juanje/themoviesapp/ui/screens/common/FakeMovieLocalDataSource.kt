package com.juanje.themoviesapp.ui.screens.common

import androidx.paging.PagingSource
import androidx.paging.testing.asPagingSourceFactory
import com.juanje.data.datasources.MovieLocalDataSource
import com.juanje.domain.dataclasses.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeMovieLocalDataSource : MovieLocalDataSource {
    private var moviesInDb = mutableListOf<Movie>()

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> getMovies(userName: String, category: String): PagingSource<Int, T> =
        moviesInDb.asPagingSourceFactory().invoke() as PagingSource<Int, T>

    override fun getMovie(businessId: String, userName: String, category: String): Flow<Movie> = flow {
        val movie = moviesInDb.firstOrNull {
            it.businessId == businessId && it.userName == userName && it.category == category
        } ?: throw NoSuchElementException("Movie with businessId $businessId not found")
        emit(movie)
    }

    override suspend fun deleteAll(userName: String, category: String) {
        moviesInDb.clear()
    }

    override suspend fun insertAll(movies: List<Movie>) {
        moviesInDb.addAll(movies)
    }
}