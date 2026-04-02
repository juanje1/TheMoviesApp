package com.juanje.data.repositories

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.juanje.data.common.asAppError
import com.juanje.data.common.safeCall
import com.juanje.data.datasources.FavoriteLocalDataSource
import com.juanje.data.datasources.MovieLocalDataSource
import com.juanje.domain.dataclasses.Favorite
import com.juanje.domain.dataclasses.Movie
import com.juanje.domain.dataclasses.MovieFavorite
import com.juanje.data.interfaces.MovieMapper
import com.juanje.data.interfaces.MovieRemoteMediatorProvider
import com.juanje.domain.repositories.MovieRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class, ExperimentalCoroutinesApi::class)
class MovieRepositoryImpl @Inject constructor(
    private val movieLocalDataSource: MovieLocalDataSource,
    private val mediatorProvider: MovieRemoteMediatorProvider,
    private val favoriteLocalDataSource: FavoriteLocalDataSource,
    private val movieMapper: MovieMapper<Any, MovieFavorite>
): MovieRepository {

    companion object {
        const val PAGE_SIZE = 20
    }

    override fun getMovies(userName: String, category: String): Flow<PagingData<MovieFavorite>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                initialLoadSize = 40,
                prefetchDistance = 15,
                enablePlaceholders = true
            ),
            remoteMediator = mediatorProvider.getMediator(userName, category),
            pagingSourceFactory = { movieLocalDataSource.getMovies(userName, category) }
        ).flow.map { pagingData -> pagingData.map { movieMapper.map(it) } }
    }

    override fun getMovie(businessId: String, userName: String, category: String): Flow<MovieFavorite> {
        return movieLocalDataSource.getMovie(businessId, userName, category).flatMapLatest { movie ->
            val favoriteFlow = favoriteLocalDataSource.getFavorite(movie.businessId)

            favoriteFlow.map { isFav ->
                MovieFavorite(movie = movie, isFavorite = isFav)
            }
        }.filterNotNull().asAppError()
    }

    override suspend fun updateMovie(movie: Movie, isFavorite: Boolean) {
        return safeCall {
            if (isFavorite) {
                val favorite = Favorite(movie.businessId)
                favoriteLocalDataSource.insertFavorite(favorite)
            } else
                favoriteLocalDataSource.deleteFavorite(movie.businessId)
        }
    }
}