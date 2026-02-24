package com.juanje.domain.dataclasses

data class Movie (
    val id: Int = 0,
    val title: String = "",
    val overview: String = "",
    val posterPath: String = "",
    val releaseDate: String = "",
    val userName: String = "",
    val displayOrder: Int = 0
)