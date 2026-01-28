package com.juanje.framework.database.datasources

import com.juanje.data.datasources.FavoriteLocalDataSource
import com.juanje.domain.dataclasses.Favorite
import com.juanje.domain.IoDispatcher
import com.juanje.framework.database.daos.FavoriteDao
import com.juanje.framework.database.dataclasses.toFavorite
import com.juanje.framework.database.dataclasses.toFavoriteDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FavoriteDatabaseDataSource @Inject constructor(
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