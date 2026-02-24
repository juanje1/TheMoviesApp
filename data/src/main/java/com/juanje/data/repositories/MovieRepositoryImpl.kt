package com.juanje.data.repositories

import com.juanje.data.datasources.FavoriteLocalDataSource
import com.juanje.data.datasources.MovieLocalDataSource
import com.juanje.data.datasources.MovieRemoteDataSource
import com.juanje.data.common.asAppError
import com.juanje.data.common.safeCall
import com.juanje.domain.dataclasses.Favorite
import com.juanje.domain.dataclasses.Movie
import com.juanje.domain.dataclasses.MovieFavorite
import com.juanje.domain.repositories.MovieRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Named
import kotlin.math.ceil

@OptIn(ExperimentalCoroutinesApi::class)
class MovieRepositoryImpl @Inject constructor(
    private val movieLocalDataSource: MovieLocalDataSource,
    private val movieRemoteDataSource: MovieRemoteDataSource,
    private val favoriteLocalDataSource: FavoriteLocalDataSource,
    @Named("apiKey") private val apiKey: String
): MovieRepository {

    companion object {
        const val PAGE_SIZE = 20
    }

    private val mutex = Mutex()

    private fun generateBusinessId(userName: String, movie: Movie): String =
        "$userName|${movie.title}|${movie.releaseDate}"

    override fun getMovieFavorites(userName: String): Flow<List<MovieFavorite>> {
        val moviesFlow = movieLocalDataSource.getMovies(userName)
        val favoritesFlow = favoriteLocalDataSource.getFavorites(userName)

        return combine(moviesFlow, favoritesFlow) { movieList, favoriteList ->
            val favoriteIdsSet = favoriteList.map { it.businessId }.toSet()
            movieList.map { movie ->
                val businessId = generateBusinessId(userName, movie)
                MovieFavorite(movie = movie, isFavorite = businessId in favoriteIdsSet)
            }
        }.filterNotNull().asAppError()
    }

    override fun getMovieFavorite(userName: String, movieId: Int): Flow<MovieFavorite> {
        return movieLocalDataSource.getMovie(userName, movieId).flatMapLatest { movie ->
            val businessId = generateBusinessId(userName, movie)
            val favoriteFlow = favoriteLocalDataSource.getFavorite(businessId)

            favoriteFlow.map { isFav ->
                MovieFavorite(movie = movie, isFavorite = isFav)
            }
        }.filterNotNull().asAppError()
    }

    override suspend fun count(userName: String): Int {
        return safeCall {
            movieLocalDataSource.count(userName)
        }
    }

    override suspend fun getAndInsertMovies(userName: String, refresh: Boolean) {
        mutex.withLock {
            return safeCall {
                val count = if (refresh) 0 else count(userName)
                val nextPage = ceil(count.toDouble() / PAGE_SIZE).toInt() + 1

                val remoteMovies = movieRemoteDataSource.getMovies(userName, apiKey, nextPage)
                val moviesToUpsert = remoteMovies.mapIndexed { index, remoteMovie ->
                    remoteMovie.copy(displayOrder = count + index + 1)
                }

                if (refresh) {
                    movieLocalDataSource.refreshMoviesTx(userName, moviesToUpsert)
                } else {
                    movieLocalDataSource.insertAll(moviesToUpsert)
                }
            }
        }
    }

    override suspend fun updateMovieFavorite(userName: String, movie: Movie, isFavorite: Boolean) {
        return safeCall {
            val businessId = generateBusinessId(userName, movie)

            if (isFavorite) {
                val favorite = Favorite(businessId)
                favoriteLocalDataSource.insertFavorite(favorite)
            } else
                favoriteLocalDataSource.deleteFavorite(businessId)
        }
    }
}