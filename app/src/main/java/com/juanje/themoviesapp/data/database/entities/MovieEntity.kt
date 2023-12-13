package com.juanje.themoviesapp.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.juanje.domain.Movie

@Entity
data class MovieEntity (
    @PrimaryKey(autoGenerate = true) val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String,
    val favourite: Boolean = false,
    val userName: String
)

fun MovieEntity.toMovie() = Movie(
    id = id,
    title = title,
    overview = overview,
    posterPath = posterPath,
    favourite = favourite,
    userName = userName
)

fun Movie.toMovieEntity() = MovieEntity(
    id = id,
    title = title,
    overview = overview,
    posterPath = posterPath,
    favourite = favourite,
    userName = userName
)