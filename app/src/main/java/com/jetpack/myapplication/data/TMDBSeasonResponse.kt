package com.jetpack.myapplication.data

import com.squareup.moshi.Json
data class TMDBSeasonResponse(
    @Json(name = "id") val id: Int,
    @Json(name = "season_number") val seasonNumber: Int? = 0,
    @Json(name = "episodes") val episodes: List<TMDBEpisode>
)

data class TMDBEpisode(
    @Json(name = "id") val id: Int,
    @Json(name = "episode_number") val episodeNumber: Int? = 0,
    @Json(name = "season_number") val seasonNumber: Int? = 0,
    @Json(name = "name") val name: String? = "Untitled",
    @Json(name = "overview") val overview: String? = "",
    @Json(name = "still_path") val stillPath: String? = null
)