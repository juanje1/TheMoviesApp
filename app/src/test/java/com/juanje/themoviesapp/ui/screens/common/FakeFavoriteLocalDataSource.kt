package com.juanje.themoviesapp.ui.screens.common

import com.juanje.data.datasources.FavoriteLocalDataSource
import com.juanje.domain.dataclasses.Favorite
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

class FakeFavoriteLocalDataSource : FavoriteLocalDataSource {
    private val favorites = MutableStateFlow<List<Favorite>>(emptyList())

    override fun getFavorite(businessId: String): Flow<Boolean> =
        favorites.map { list -> list.any { it.businessId == businessId } }

    override suspend fun insertFavorite(favorite: Favorite) {
        val newList = favorites.value.toMutableList()

        newList.removeAll { it.businessId == favorite.businessId }
        newList.add(favorite)

        favorites.value = newList
    }

    override suspend fun deleteFavorite(businessId: String) {
        favorites.value = favorites.value.filterNot { it.businessId == businessId }
    }

    fun getAllFavorites(): StateFlow<List<Favorite>> = favorites.asStateFlow()
}