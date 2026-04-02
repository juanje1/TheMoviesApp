package com.juanje.data.interfaces

interface MovieMapper<I, O> {
    fun map(input: I): O
}