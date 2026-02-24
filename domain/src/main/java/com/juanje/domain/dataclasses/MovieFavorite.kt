package com.juanje.domain.dataclasses

data class MovieFavorite (
    val movie: Movie = Movie(),
    val isFavorite: Boolean = false
)