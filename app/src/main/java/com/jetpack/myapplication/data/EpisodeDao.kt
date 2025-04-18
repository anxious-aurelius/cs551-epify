package com.jetpack.myapplication.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface EpisodeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(episode: WatchedEpisodeEntity)

    @Query("SELECT * FROM watched_episodes WHERE showId = :showId")
    suspend fun getWatchedForShow(showId: Int): List<WatchedEpisodeEntity>

    @Query("SELECT * FROM watched_episodes WHERE episodeId = :id")
    suspend fun getEpisodeById(id: Int): WatchedEpisodeEntity?

    @Query("UPDATE watched_episodes SET isWatched = NOT isWatched WHERE episodeId = :id")
    suspend fun toggleWatchedStatus(id: Int)
}
