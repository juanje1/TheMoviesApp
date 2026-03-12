package com.juanje.domain.dataclasses

data class Page (
    val userName: String = "",
    val category: String = "",
    val nextPage: Int? = null
)