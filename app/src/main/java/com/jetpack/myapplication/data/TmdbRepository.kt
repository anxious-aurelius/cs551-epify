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


}
