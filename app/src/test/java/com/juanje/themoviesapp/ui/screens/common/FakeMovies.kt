package com.juanje.themoviesapp.ui.screens.common

import com.juanje.data.datasources.FavoriteLocalDataSource
import com.juanje.data.datasources.MovieLocalDataSource
import com.juanje.data.datasources.MovieRemoteDataSource
import com.juanje.domain.dataclasses.Favorite
import com.juanje.domain.dataclasses.Movie
import com.juanje.domain.dataclasses.MovieFavorite
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

const val fakeUserName = "Juan"
const val fakeId = 5

val fakeMovie = createFakeMovie(5, 5)
val fakeMovies = (1..10).map { createFakeMovie(id = it, order = it) }

val fakeFavorites = (1..2).map { createFakeFavorite(it) }

val fakeMovieFavorite = (1..10).map { createFakeMovieFavorite(it, false) }
val fakeMovieFavoriteNoFavorite = (1..10).map { createFakeMovieFavorite(it, true) }

val fakeFavoritesId = listOf(1, 2)

fun createFakeMovie(id: Int, order: Int) = Movie(
    id = id,
    title = "Title $id",
    overview = "Overview $id",
    posterPath = "Path $id",
    releaseDate = "Date $id",
    userName = fakeUserName,
    displayOrder = order
)

fun createFakeFavorite(id: Int) = Favorite(
    businessId = "$fakeUserName|Title $id|Date $id"
)

fun createFakeMovieFavorite(id: Int, noFavorite: Boolean) = MovieFavorite(
    movie = fakeMovies[id-1],
    isFavorite = if (noFavorite) false else fakeFavorites.contains(createFakeFavorite(id))
)

class FakeMovieLocalDataSource : MovieLocalDataSource {
    private var moviesFlow = MutableStateFlow<List<Movie>>(emptyList())
    private var movieFlow = MutableStateFlow(fakeMovie)

    override fun getMovies(userName: String) = moviesFlow

    override fun getMovie(userName: String, movieId: Int) = movieFlow

    override suspend fun count(userName: String): Int = moviesFlow.value.size

    override suspend fun deleteAll(userName: String) {
        moviesFlow.value = emptyList()
    }

    override suspend fun insertAll(movies: List<Movie>) {
        val currentMovies = moviesFlow.value.toMutableList()

        movies.forEach { movie ->
            val existingIndex = movies.find {
                it.userName == movie.userName &&
                it.title == movie.title &&
                it.releaseDate == movie.releaseDate
            }

            if (existingIndex != null)
                currentMovies.remove(movie)

            val nextId = if (currentMovies.isEmpty()) 1 else currentMovies.maxOf { it.id } + 1
            currentMovies.add(movie.copy(id = nextId))
        }
        moviesFlow.value = currentMovies
    }

    override suspend fun refreshMoviesTx(userName: String, movies: List<Movie>) {
        deleteAll(userName)
        insertAll(movies)
    }
}

class FakeMovieRemoteDataSource : MovieRemoteDataSource {
    override suspend fun getMovies(userName: String, apiKey: String, page: Int) = fakeMovies
}

class FakeFavoriteLocalDataSource : FavoriteLocalDataSource {
    private var favorites = MutableStateFlow<List<Favorite>>(emptyList())

    override fun getFavorites(userName: String): Flow<List<Favorite>> =
        favorites.map { list -> list.filter { it.businessId.startsWith("$userName|") } }

    override fun getFavorite(businessId: String): Flow<Boolean> =
        favorites.map { list -> list.any { it.businessId == businessId } }

    override suspend fun insertFavorite(favorite: Favorite) {
        val newList = favorites.value.toMutableList()

        newList.removeAll { it.businessId == favorite.businessId }
        newList.add(favorite)

        favorites.value = newList
    }

    override suspend fun deleteFavorite(businessId: String) {
        favorites.value = favorites.value.filterNot { it.businessId == businessId }
    }

    fun isFavorite(businessId: String): Boolean =
        favorites.value.any { it.businessId == businessId }
}