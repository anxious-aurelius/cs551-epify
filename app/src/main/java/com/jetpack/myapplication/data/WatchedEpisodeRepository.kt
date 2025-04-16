package com.jetpack.myapplication.data


import com.jetpack.myapplication.data.EpisodeDao
import com.jetpack.myapplication.data.WatchedEpisodeEntity

class WatchedEpisodeRepository(private val dao: EpisodeDao) {
    suspend fun markAsWatched(entity: WatchedEpisodeEntity) = dao.upsert(entity)
    suspend fun getWatchedForShow(showId: Int): List<WatchedEpisodeEntity> = dao.getWatchedForShow(showId)
}