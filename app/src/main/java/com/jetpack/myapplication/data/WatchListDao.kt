package com.jetpack.myapplication.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchlistDao {
    @Query("SELECT * FROM watchlist ORDER BY id")
    fun getAll(): Flow<List<WatchlistEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: WatchlistEntity)

    @Delete
    suspend fun delete(item: WatchlistEntity)
}
