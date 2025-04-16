package com.jetpack.myapplication.api
import com.jetpack.myapplication.application.SearchResult
import com.jetpack.myapplication.application.Show
import com.jetpack.myapplication.application.TrendingShow
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TraktService {
    // Fetch trending shows
    @GET("shows/trending")
    suspend fun getTrendingShows(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10
    ): List<TrendingShow>

    // Fetch popular shows
    @GET("shows/popular")
    suspend fun getPopularShows(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10
    ): List<Show>

    @GET("shows/{id}?extended=full")
    suspend fun getShowDetails(
        @Path("id") traktId: String
    ): Show

    @GET("search/show")
    suspend fun searchEverything(
        @Query("query") query: String,
        @Query("limit") limit: Int = 20,
        @Query("extended") extended: String = "full"
    ): List<SearchResult>

}
