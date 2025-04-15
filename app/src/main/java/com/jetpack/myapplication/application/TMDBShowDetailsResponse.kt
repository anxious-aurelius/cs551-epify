package com.jetpack.myapplication.application

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TMDBShowDetailsResponse(
    @Json(name = "id")
    val id: Int? = null,

    @Json(name = "name")
    val name: String? = null,

    @Json(name = "overview")
    val overview: String? = null,

    @Json(name = "backdrop_path")
    val backdropPath: String? = null,

    @Json(name = "poster_path")
    val posterPath: String? = null,

    @Json(name = "first_air_date")
    val firstAirDate: String? = null,

    @Json(name = "vote_average")
    val voteAverage: Double? = null,

    @Json(name = "number_of_seasons")
    val numberOfSeasons: Int? = null,

    @Json(name = "number_of_episodes")
    val numberOfEpisodes: Int? = null,

    @Json(name = "genres")
    val genres: List<Genre>? = emptyList()
)

@JsonClass(generateAdapter = true)
data class Genre(
    @Json(name = "id")
    val id: Int? = null,

    @Json(name = "name")
    val name: String? = null
)
