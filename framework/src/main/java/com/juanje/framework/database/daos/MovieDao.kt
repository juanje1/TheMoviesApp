package com.juanje.framework.database.daos

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.juanje.framework.database.dataclasses.MovieDatabase
import com.juanje.framework.database.dataclasses.MovieFavoriteDatabase
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Query("""
        SELECT MovieDatabase.*, (FavoriteDatabase.businessId IS NOT NULL) AS isFavorite
        FROM MovieDatabase
        LEFT JOIN FavoriteDatabase ON MovieDatabase.businessId = FavoriteDatabase.businessId
        WHERE MovieDatabase.userName = :userName
          AND MovieDatabase.category = :category
        ORDER BY displayOrder ASC
    """)
    fun getMovies(userName: String, category: String): PagingSource<Int, MovieFavoriteDatabase>

    @Query("SELECT * FROM MovieDatabase WHERE businessId = :businessId AND userName = :userName AND category = :category")
    fun getMovie(businessId: String, userName: String, category: String): Flow<MovieDatabase>

    @Query("DELETE FROM MovieDatabase WHERE userName = :userName AND category = :category")
    suspend fun deleteAll(userName: String, category: String)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(movies: List<MovieDatabase>)
}