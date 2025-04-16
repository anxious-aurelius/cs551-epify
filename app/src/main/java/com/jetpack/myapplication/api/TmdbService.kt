package com.jetpack.myapplication.api


import com.jetpack.myapplication.application.TMDBShowDetailsResponse
import com.jetpack.myapplication.data.TMDBSeasonResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDBService {
    // Fetch TV show details by TMDB ID
    @GET("tv/{tv_id}")
    suspend fun getTvDetails(
        @Path("tv_id") tvId: Int,
        @Query("api_key") apiKey: String
    ): TMDBShowDetailsResponse

    @GET("tv/{tv_id}/season/{season_number}")
    suspend fun getSeasonDetails(
        @Path("tv_id") tvId: Int,
        @Path("season_number") seasonNumber: Int,
        @Query("api_key") apiKey: String
    ): TMDBSeasonResponse

}
