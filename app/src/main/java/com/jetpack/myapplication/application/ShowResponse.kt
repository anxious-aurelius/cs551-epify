package com.jetpack.myapplication.application
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TrendingShow(
    val watchers: Int,
    val show: Show
)

@JsonClass(generateAdapter = true)
data class Show(
    val title: String,
    val year: Int,
    val ids: Ids
)

@JsonClass(generateAdapter = true)
data class Ids(
    val trakt: Int,
    val slug: String,
    val tvdb: Int,
    val imdb: String,
    val tmdb: Int,
    @Json(name = "tvrage") val tvRage: Int?
)
