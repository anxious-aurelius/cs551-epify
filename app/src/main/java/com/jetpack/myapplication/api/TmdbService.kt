package com.jetpack.myapplication.api


import com.jetpack.myapplication.application.TMDBShowDetailsResponse
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


}
