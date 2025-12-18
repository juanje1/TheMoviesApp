package com.juanje.themoviesapp.data.database.dataclasses

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.juanje.domain.Movie

@Entity(
    indices = [
        Index(value = ["userName", "title", "releaseDate"], unique = true)
    ]
)
data class MovieDatabase (
    @PrimaryKey(autoGenerate = true) val id: Int,
    val title: String ?= null,
    val overview: String ?= null,
    val posterPath: String ?= null,
    val releaseDate: String ?= null,
    val userName: String,
    val displayOrder: Int
)

fun MovieDatabase.toMovie() = Movie(
    id = id,
    title = title,
    overview = overview,
    posterPath = posterPath,
    releaseDate = releaseDate,
    userName = userName,
    displayOrder = displayOrder
)

fun Movie.toMovieDatabase() = MovieDatabase(
    id = id,
    title = title,
    overview = overview,
    posterPath = posterPath,
    releaseDate = releaseDate,
    userName = userName,
    displayOrder = displayOrder
)