package com.juanje.framework.server.dataclasses

import com.juanje.domain.dataclasses.Movie

data class MovieResultServer(
    val adult: Boolean? = false,
    val backdrop_path: String? = "",
    val genre_ids: List<Int> = emptyList(),
    val id: Int? = 0,
    val original_language: String? = "",
    val original_title: String? = "",
    val overview: String? = "",
    val popularity: Double? = 0.0,
    val poster_path: String? = "",
    val release_date: String? = "",
    val title: String? = "",
    val video: Boolean? = false,
    val vote_average: Double? = 0.0,
    val vote_count: Int? = 0
)

fun MovieResultServer.toMovie(userName: String) = Movie(
    id = 0,
    title = title ?: "",
    overview = overview ?: "",
    posterPath = poster_path ?: "",
    releaseDate = release_date ?: "",
    userName = userName,
    displayOrder = 0
)