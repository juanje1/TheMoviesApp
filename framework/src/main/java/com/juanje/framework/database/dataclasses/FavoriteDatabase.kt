package com.juanje.framework.database.dataclasses

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.juanje.domain.dataclasses.Favorite

@Entity
data class FavoriteDatabase (
    @PrimaryKey val businessId: String
)

fun FavoriteDatabase.toFavorite() = Favorite(
    businessId = businessId
)

fun Favorite.toFavoriteDatabase() = FavoriteDatabase(
    businessId = businessId
)