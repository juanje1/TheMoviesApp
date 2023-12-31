package com.juanje.domain

data class Movie (
    val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String,
    val favourite: Boolean = false,
    val userName: String
)