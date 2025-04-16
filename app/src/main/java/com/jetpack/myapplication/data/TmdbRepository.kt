package com.jetpack.myapplication.data

import com.jetpack.myapplication.api.TMDBService
import com.jetpack.myapplication.application.TMDBRetrofitClient
import com.jetpack.myapplication.application.TMDBShowDetailsResponse

class TMDBRepository {
    private val tmdbService = TMDBRetrofitClient.instance.create(TMDBService::class.java)
    private val apiKey = "35151c780dcfad827979b740ba071cab"

    // NEW: Generic method to get full TV Details (not just posterPath)
    suspend fun getTvDetails(tmdbId: Int): TMDBShowDetailsResponse {
        return tmdbService.getTvDetails(tmdbId, apiKey)
    }

    // Existing: still if you want only PosterPath specifically
    suspend fun getPosterPath(tmdbId: Int): String? {
        val response = getTvDetails(tmdbId)
        return response.posterPath
    }
    suspend fun getSeasonDetails(tmdbId: Int, seasonNumber: Int): TMDBSeasonResponse {
        return tmdbService.getSeasonDetails(tmdbId, seasonNumber, apiKey)
    }
    suspend fun searchShows(
        title: String,
        year: Int?,
        includeAdult: Boolean,
        language: String
    ): List<TmdbShow> {
        val response = tmdbService.searchTvShows(
            title = title,
            year = year,
            includeAdult = includeAdult,
            language = language,
            apiKey = apiKey
        )
        return response.results
    }
    suspend fun getGenres(): List<TmdbGenre> {
        return tmdbService.getGenres(language = "en-US", apiKey = apiKey).genres
    }

    suspend fun discoverShows(
        genreId: Int?,
        year: Int?,
        sortBy: String,
        includeAdult: Boolean,
        language: String
    ): List<TmdbShow> {
        return tmdbService.discoverShows(
            apiKey = apiKey,
            sortBy = sortBy,
            withGenres = genreId,
            year = year,
            includeAdult = includeAdult,
            language = language
        ).results
    }


}
