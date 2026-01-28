package com.juanje.data.datasources

import com.juanje.domain.dataclasses.Favorite
import kotlinx.coroutines.flow.Flow

interface FavoriteLocalDataSource {
    fun getFavorites(userName: String): Flow<List<Favorite>>
    fun getFavorite(businessId: String): Flow<Boolean>
    suspend fun insertFavorite(favorite: Favorite)
    suspend fun deleteFavorite(businessId: String)
}