package com.juanje.data.repositories

import com.juanje.data.datasources.FavoriteLocalDataSource
import com.juanje.data.datasources.MovieLocalDataSource
import com.juanje.data.datasources.MovieRemoteDataSource
import com.juanje.domain.Favorite
import com.juanje.domain.Movie
import com.juanje.domain.MovieFavorite
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

class MovieRepository(
    private val movieLocalDataSource: MovieLocalDataSource,
    private val movieRemoteDataSource: MovieRemoteDataSource,
    private val favoriteLocalDataSource: FavoriteLocalDataSource,
    private val apiKey: String
) {
    companion object {
        const val PAGE_SIZE = 20
    }

    private fun generateBusinessId(userName: String, movie: Movie): String =
        "$userName|${movie.title}|${movie.releaseDate}"

    fun getMovieFavorites(userName: String): Flow<List<MovieFavorite>> {
        val moviesFlow = movieLocalDataSource.getMovies(userName)
        val favoritesFlow = favoriteLocalDataSource.getFavorites(userName)

        return combine(moviesFlow, favoritesFlow) { movieList, favoriteList ->
            val favoriteIdsSet = favoriteList.map { it.businessId }.toSet()
            movieList.map { movie ->
                val businessId = generateBusinessId(userName, movie)
                MovieFavorite(movie = movie, isFavorite = businessId in favoriteIdsSet)
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getMovieFavorite(userName: String, movieId: Int): Flow<MovieFavorite> {
        return movieLocalDataSource.getMovie(userName, movieId)
            .flatMapLatest { movie ->
                val businessId = generateBusinessId(userName, movie)
                val favoriteFlow = favoriteLocalDataSource.getFavorite(businessId)

                favoriteFlow.map { isFav ->
                    MovieFavorite(movie = movie, isFavorite = isFav)
                }
            }.filterNotNull()
    }

    suspend fun count(userName: String): Int =
        movieLocalDataSource.count(userName)

    suspend fun getAndInsertMovies(userName: String, refresh: Boolean = false) {
        val count = if (refresh) 0 else count(userName)
        val nextPage = (count / PAGE_SIZE) + 1

        val remoteMovies = movieRemoteDataSource.getMovies(userName, apiKey, nextPage)
        val moviesToUpsert = remoteMovies.mapIndexed { index, remoteMovie ->
            remoteMovie.copy(id = 0, displayOrder = count + index)
        }

        if (refresh) movieLocalDataSource.refreshMoviesTx(userName, moviesToUpsert)
        else movieLocalDataSource.insertAll(moviesToUpsert)
    }

    suspend fun updateMovie(userName: String, movie: Movie, isFavorite: Boolean) {
        val businessId = generateBusinessId(userName, movie)
        if (isFavorite) {
            val favorite = Favorite(businessId)
            favoriteLocalDataSource.insertFavorite(favorite)
        } else
            favoriteLocalDataSource.deleteFavorite(businessId)
    }
}