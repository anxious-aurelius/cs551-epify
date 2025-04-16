package com.jetpack.myapplication.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TmdbSearchResponse(
    @Json(name = "results") val results: List<TmdbShow>
)

@JsonClass(generateAdapter = true)
data class TmdbShow(
    val id: Int,
    val name: String,
    @Json(name = "overview") val overview: String?,
    @Json(name = "first_air_date") val firstAirDate: String?,
    @Json(name = "poster_path") val posterPath: String?,
    @Json(name = "genre_ids") val genreIds: List<Int>  )

@JsonClass(generateAdapter = true)
data class TmdbGenre(
    val id: Int,
    val name: String
)

@JsonClass(generateAdapter = true)
data class TmdbGenreResponse(
    @Json(name = "genres")
    val genres: List<TmdbGenre>
)