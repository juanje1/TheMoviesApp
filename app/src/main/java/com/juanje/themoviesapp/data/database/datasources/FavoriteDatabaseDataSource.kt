package com.juanje.themoviesapp.data.database.datasources

import com.juanje.data.datasources.FavoriteLocalDataSource
import com.juanje.domain.Favorite
import com.juanje.themoviesapp.data.IoDispatcher
import com.juanje.themoviesapp.data.database.daos.FavoriteDao
import com.juanje.themoviesapp.data.database.dataclasses.toFavorite
import com.juanje.themoviesapp.data.database.dataclasses.toFavoriteDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class FavoriteDatabaseDataSource(
    private val favoriteDao: FavoriteDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
): FavoriteLocalDataSource {

    override fun getFavorites(userName: String): Flow<List<Favorite>> =
        favoriteDao.getFavorites(userName).map { list -> list.map { it.toFavorite() } }

    override fun getFavorite(businessId: String): Flow<Boolean> =
        favoriteDao.getFavorite(businessId)

    override suspend fun insertFavorite(favorite: Favorite) =
        withContext(ioDispatcher) {
            favoriteDao.insertFavorite(favorite.toFavoriteDatabase())
        }

    override suspend fun deleteFavorite(businessId: String) =
        withContext(ioDispatcher) {
            favoriteDao.deleteFavorite(businessId)
        }
}