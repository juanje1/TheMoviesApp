package com.juanje.framework.database.implementations

import com.juanje.domain.dataclasses.MovieFavorite
import com.juanje.domain.interfaces.Mapper
import com.juanje.framework.database.dataclasses.MovieFavoriteDatabase
import com.juanje.framework.database.dataclasses.toMovie
import javax.inject.Inject

class MovieMapperImpl @Inject constructor() : Mapper<Any, MovieFavorite> {

    override fun map(input: Any): MovieFavorite {
        val entity = input as MovieFavoriteDatabase
        return MovieFavorite(movie = entity.movie.toMovie(), isFavorite = entity.isFavorite)
    }
}