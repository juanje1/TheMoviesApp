package com.juanje.domain.dataclasses

data class Movie (
    val businessId: String = "",
    val title: String = "",
    val overview: String = "",
    val posterPath: String = "",
    val releaseDate: String = "",
    val userName: String = "",
    val category: String = "",
    val displayOrder: Int = 0
)