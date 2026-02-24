package com.juanje.framework.database.dataclasses

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.juanje.domain.dataclasses.Movie

@Entity(
    indices = [
        Index(value = ["userName", "title", "releaseDate"], unique = true)
    ]
)
data class MovieDatabase (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val overview: String,
    val posterPath: String,
    val releaseDate: String,
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