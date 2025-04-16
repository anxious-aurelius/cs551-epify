package com.jetpack.myapplication.api


import com.jetpack.myapplication.application.TMDBShowDetailsResponse
import com.jetpack.myapplication.data.TMDBSeasonResponse
import com.jetpack.myapplication.data.TmdbGenreResponse
import com.jetpack.myapplication.data.TmdbSearchResponse
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

    @GET("search/tv")
    suspend fun searchTvShows(
        @Query("query") title: String,
        @Query("first_air_date_year") year: Int? = null,
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1,
        @Query("api_key") apiKey: String
    ): TmdbSearchResponse
    @GET("genre/tv/list")
    suspend fun getGenres(
        @Query("language") language: String = "en-US",
        @Query("api_key") apiKey: String
    ): TmdbGenreResponse

    @GET("discover/tv")
    suspend fun discoverShows(
        @Query("api_key") apiKey: String,
        @Query("sort_by") sortBy: String,
        @Query("with_genres") withGenres: Int?,
        @Query("first_air_date_year") year: Int?,
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): TmdbSearchResponse

}
