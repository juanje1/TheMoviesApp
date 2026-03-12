package com.juanje.themoviesapp.ui.screens.common

import androidx.paging.PagingSource
import androidx.paging.testing.asPagingSourceFactory
import com.juanje.data.datasources.FavoriteLocalDataSource
import com.juanje.data.datasources.MovieLocalDataSource
import com.juanje.domain.MovieFactory.FAKE_CATEGORY
import com.juanje.domain.MovieFactory.FAKE_USER_NAME
import com.juanje.domain.dataclasses.Favorite
import com.juanje.domain.dataclasses.Movie
import com.juanje.domain.dataclasses.MovieFavorite
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

val fakeMoviesList = (1..10).map {
    createFakeMovie(index = it, order = it)
}
val fakeMovieWithFavoritesList = (1..10).map {
    index -> createFakeMovieFavorite(index, favorite = index <= 2)
}
val fakeMovieWithoutFavoritesList = (1..10).map {
    index -> createFakeMovieFavorite(index, favorite = false)
}

fun createFakeMovie(index: Int, order: Int)  = Movie(
    businessId = generateBusinessId(index),
    title = "Title $index",
    overview = "Overview $index",
    posterPath = "Path $index",
    releaseDate = "Date $index",
    userName = FAKE_USER_NAME,
    category = FAKE_CATEGORY,
    displayOrder = order
)

fun createFakeMovieFavorite(index: Int, favorite: Boolean) = MovieFavorite(
    movie = fakeMoviesList[index-1],
    isFavorite = favorite
)

fun generateBusinessId(index: Int): String {
    val title = "Title $index".trim().lowercase()
    val releaseDate = "Date $index".trim().lowercase()

    return "${FAKE_USER_NAME.lowercase()}|${FAKE_CATEGORY.lowercase()}|$title|$releaseDate"
}

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

class FakeFavoriteLocalDataSource : FavoriteLocalDataSource {
    private var favorites = MutableStateFlow<List<Favorite>>(emptyList())

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
}