package com.jetpack.myapplication.showDetails

data class ShowDetail(
    val id: Int,
    val title: String,
    val overview: String,
    val backdropUrl: String?,
    val posterUrl: String?,
    val year: Int,
    val genres: List<String> = emptyList(),
    val rating: Double = 0.0,
    val seasons: Int = 0,
    val episodes: Int = 0,
    val cast: List<CastMember> = emptyList(),
    val episodeList: List<Episode> = emptyList(),
    val selectedSeason: Int = 1
)

data class CastMember(
    val name: String,
    val character: String,
    val photoUrl: String?
)

data class Episode(
    val id: Int,
    val seasonNumber: Int,
    val episodeNumber: Int,
    val title: String,
    val overview: String,
    val imageUrl: String?,
    val isWatched: Boolean = false // Default value false
)

