package com.juanje.framework.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.juanje.framework.database.dataclasses.PageDatabase

@Dao
interface PageDao {
    @Query("SELECT * FROM PageDatabase WHERE userName = :userName AND category = :category")
    suspend fun getPage(userName: String, category: String): PageDatabase?

    @Query("DELETE FROM PageDatabase WHERE userName = :userName AND category = :category")
    suspend fun deletePage(userName: String, category: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPage(page: PageDatabase)
}