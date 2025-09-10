package com.juanje.themoviesapp.data.database.dataclasses

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.juanje.domain.Movie

@Entity
data class MovieDatabase (
    @PrimaryKey(autoGenerate = true) val id: Int,
    val title: String ?= null,
    val overview: String ?= null,
    val posterPath: String ?= null,
    val favourite: Boolean = false,
    val userName: String
)

fun MovieDatabase.toMovie() = Movie(
    id = id,
    title = title,
    overview = overview,
    posterPath = posterPath,
    favourite = favourite,
    userName = userName
)

fun Movie.toMovieDatabase() = MovieDatabase(
    id = id,
    title = title,
    overview = overview,
    posterPath = posterPath,
    favourite = favourite,
    userName = userName
)