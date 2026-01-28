package com.juanje.framework.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.juanje.framework.database.dataclasses.FavoriteDatabase
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM FavoriteDatabase WHERE businessId LIKE :userName || '|%'")
    fun getFavorites(userName: String): Flow<List<FavoriteDatabase>>

    @Query("SELECT EXISTS(SELECT 1 FROM FavoriteDatabase WHERE businessId = :businessId)")
    fun getFavorite(businessId: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteDatabase)

    @Query("DELETE FROM FavoriteDatabase WHERE businessId = :businessId")
    suspend fun deleteFavorite(businessId: String)
}