package com.juanje.domain.dataclasses

data class Movie (
    val id: Int,
    val title: String ?= null,
    val overview: String ?= null,
    val posterPath: String ?= null,
    val releaseDate: String ?= null,
    val userName: String,
    val displayOrder: Int
)