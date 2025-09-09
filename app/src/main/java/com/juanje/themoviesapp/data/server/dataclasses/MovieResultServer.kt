package com.juanje.themoviesapp.data.server.dataclasses

import com.juanje.domain.Movie

data class MovieResultServer(
    val adult: Boolean,
    val backdropPath: String,
    val genreIds: List<Int>,
    val id: Int,
    val originalLanguage: String,
    val originalTitle: String,
    val overview: String,
    val popularity: Double,
    val posterPath: String,
    val releaseDate: String,
    val title: String,
    val video: Boolean,
    val voteAverage: Double,
    val voteCount: Int,
    val favourite: Boolean = false
)

fun MovieResultServer.toMovie(userName: String) = Movie(
    id = 0,
    title = title,
    overview = overview,
    posterPath = posterPath,
    favourite = favourite,
    userName = userName
)