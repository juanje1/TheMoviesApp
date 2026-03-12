package com.juanje.framework.database.dataclasses

import androidx.room.Embedded

data class MovieFavoriteDatabase(
    @Embedded val movie: MovieDatabase,
    val isFavorite: Boolean
)