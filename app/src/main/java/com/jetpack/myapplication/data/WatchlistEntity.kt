package com.jetpack.myapplication.data


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "watchlist")
data class WatchlistEntity(
    @PrimaryKey val id: String,
    val title: String,
    val posterUrl: String
)
