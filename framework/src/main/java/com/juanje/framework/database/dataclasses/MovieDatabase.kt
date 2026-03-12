package com.juanje.framework.database.dataclasses

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.juanje.domain.dataclasses.Movie

@Entity(
    indices = [
        Index(value = ["userName", "category", "title", "releaseDate"], unique = true)
    ]
)
data class MovieDatabase (
    @PrimaryKey val businessId: String,
    val title: String,
    val overview: String,
    val posterPath: String,
    val releaseDate: String,
    val userName: String,
    val category: String,
    val displayOrder: Int
)

fun MovieDatabase.toMovie() = Movie(
    businessId = businessId,
    title = title,
    overview = overview,
    posterPath = posterPath,
    releaseDate = releaseDate,
    userName = userName,
    category = category,
    displayOrder = displayOrder
)

fun Movie.toMovieDatabase() = MovieDatabase(
    businessId = businessId,
    title = title,
    overview = overview,
    posterPath = posterPath,
    releaseDate = releaseDate,
    userName = userName,
    category = category,
    displayOrder = displayOrder
)