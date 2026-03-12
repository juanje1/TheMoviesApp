package com.juanje.domain.repositories

import androidx.paging.PagingData
import com.juanje.domain.dataclasses.Movie
import com.juanje.domain.dataclasses.MovieFavorite
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getMovies(userName: String, category: String): Flow<PagingData<MovieFavorite>>
    fun getMovie(businessId: String, userName: String, category: String): Flow<MovieFavorite>
    suspend fun updateMovie(movie: Movie, isFavorite: Boolean)
}