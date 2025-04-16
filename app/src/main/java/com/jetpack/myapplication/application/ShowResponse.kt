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
    val year: Int? = null,
    val ids: Ids
)

@JsonClass(generateAdapter = true)
data class Ids(
    val trakt: Int,
    val slug: String,
    val tvdb: Int? = null,
    val imdb: String? = null,
    val tmdb: Int? = null,
    @Json(name = "tvrage") val tvRage: Int? = null
)

@JsonClass(generateAdapter = true)
data class SearchResult(
    val type: String,
    val score: Double,
    val show: Show? = null,   // Only using "show" type results
    val movie: Show? = null   // Optional: in case you want movies too (reusing Show structure)
)