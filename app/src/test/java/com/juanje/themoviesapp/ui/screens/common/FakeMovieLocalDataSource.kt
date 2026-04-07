package com.juanje.themoviesapp.ui.screens.common

import androidx.paging.PagingSource
import androidx.paging.testing.asPagingSourceFactory
import com.juanje.data.datasources.MovieLocalDataSource
import com.juanje.domain.dataclasses.Movie
import com.juanje.domain.dataclasses.MovieFavorite
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeMovieLocalDataSource : MovieLocalDataSource {
    private var moviesInDb = mutableListOf<MovieFavorite>()

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> getMovies(userName: String, category: String): PagingSource<Int, T> =
        moviesInDb.asPagingSourceFactory().invoke() as PagingSource<Int, T>

    override fun getMovie(businessId: String, userName: String, category: String): Flow<Movie> = flow {
        val movieFavorite = moviesInDb.firstOrNull {
            it.movie.businessId == businessId && it.movie.userName == userName && it.movie.category == category
        } ?: throw NoSuchElementException("Movie with businessId $businessId not found")
        emit(movieFavorite.movie)
    }

    override suspend fun deleteAll(userName: String, category: String) {
        moviesInDb.clear()
    }

    override suspend fun insertAll(movies: List<Movie>) {
        val movieFavoriteList = movies.map { MovieFavorite(movie = it) }
        moviesInDb.addAll(movieFavoriteList)
    }
}