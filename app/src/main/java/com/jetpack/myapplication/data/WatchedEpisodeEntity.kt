package com.jetpack.myapplication.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "watched_episodes")
data class WatchedEpisodeEntity(
    @PrimaryKey val episodeId: Int,
    val showId: Int,
    val season: Int,
    val episode: Int,
    val title: String,
    val isWatched: Boolean
)

