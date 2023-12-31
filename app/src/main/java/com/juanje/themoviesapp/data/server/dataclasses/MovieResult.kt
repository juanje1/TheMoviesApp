package com.juanje.themoviesapp.data.server.dataclasses

data class MovieResult(
    val page: Int,
    val results: List<MovieServer>,
    val total_pages: Int,
    val total_results: Int
)