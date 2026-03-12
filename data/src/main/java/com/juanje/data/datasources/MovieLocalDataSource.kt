package com.juanje.data.datasources

import androidx.paging.PagingSource
import com.juanje.domain.dataclasses.Movie
import kotlinx.coroutines.flow.Flow

interface MovieLocalDataSource {
    fun <T : Any> getMovies(userName: String, category: String): PagingSource<Int, T>
    fun getMovie(businessId: String, userName: String, category: String): Flow<Movie>
    suspend fun deleteAll(userName: String, category: String)
    suspend fun insertAll(movies: List<Movie>)
}