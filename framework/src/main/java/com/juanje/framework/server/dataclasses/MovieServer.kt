package com.juanje.framework.server.dataclasses

data class MovieServer(
    val page: Int,
    val results: List<MovieResultServer>,
    val totalPages: Int,
    val totalResults: Int
)