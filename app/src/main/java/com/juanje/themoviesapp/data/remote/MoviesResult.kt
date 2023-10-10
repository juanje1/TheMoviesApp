package com.juanje.themoviesapp.data.remote

data class MoviesResult(
    val page: Int,
    val results: List<ServerMovie>,
    val total_pages: Int,
    val total_results: Int
)