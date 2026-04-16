package com.juanje.domain

import com.juanje.domain.dataclasses.Movie

object MovieFactory {
    private const val FAKE_TOTAL_MOVIES = 10

    const val FAKE_USER_NAME = "Juan"
    const val FAKE_CATEGORY = "popularity.desc"
    const val FAKE_ID_DETAIL = 5
    const val FAKE_ID_FAVORITE = 5
    const val FAKE_LAST_ID_PAGE_1 = 19
    const val FAKE_LAST_ID_PAGE_2 = 39
    const val FAKE_API_KEY = "d30e1f350220f9aad6c4110df385d380"

    val fakeMoviesList = createFakeMovies(quantity = FAKE_TOTAL_MOVIES)

    private fun createFakeMovie(index: Int, order: Int) = Movie(
        businessId = generateBusinessId(index),
        title = "Title $index",
        overview = "Overview $index",
        posterPath = "Path $index",
        releaseDate = "Date $index",
        userName = FAKE_USER_NAME,
        category = FAKE_CATEGORY,
        displayOrder = order
    )

    fun createFakeMovies(init: Int = 1, quantity: Int = 20): List<Movie> {
        val movies = mutableListOf<Movie>()

        for(index in init..init - 1 + quantity) {
            movies.add(createFakeMovie(index, index))
        }

        return movies
    }

    fun generateBusinessId(index: Int): String {
        val title = "Title $index".trim().lowercase()
        val releaseDate = "Date $index".trim().lowercase()

        return "${FAKE_USER_NAME.lowercase()}|${FAKE_CATEGORY.lowercase()}|$title|$releaseDate"
    }
}