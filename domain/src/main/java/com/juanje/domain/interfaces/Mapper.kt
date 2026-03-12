package com.juanje.domain.interfaces

interface Mapper<I, O> {
    fun map(input: I): O
}