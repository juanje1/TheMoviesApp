package com.juanje.usecases

import androidx.paging.PagingData
import com.juanje.domain.dataclasses.Movie
import com.juanje.domain.dataclasses.MovieFavorite
import com.juanje.domain.repositories.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoadMovie @Inject constructor(private val movieRepository: MovieRepository) {

    fun invokeGetMovies(userName: String, category: String): Flow<PagingData<MovieFavorite>> =
        movieRepository.getMovies(userName, category)

    fun invokeGetMovie(businessId: String, userName: String, category: String): Flow<MovieFavorite> =
        movieRepository.getMovie(businessId, userName, category)

    suspend fun invokeUpdateMovie(movie: Movie, isFavorite: Boolean): Unit =
        movieRepository.updateMovie(movie, isFavorite)
}