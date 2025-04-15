package com.jetpack.myapplication.notification

data class Episode(
    val showName: String,
    val seasonNumber: Int,
    val episodeNumber: Int,
    val rating: Double,
    val releaseInfo: String,
    val overview: String,
    val imageUrl: String,
    val isReleased: Boolean,
//    val isInWatchlist: Boolean = false
)