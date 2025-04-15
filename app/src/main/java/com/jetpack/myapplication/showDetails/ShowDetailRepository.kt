package com.jetpack.myapplication.showDetails

import com.jetpack.myapplication.api.TraktService
import com.jetpack.myapplication.application.RetrofitClient
import com.jetpack.myapplication.application.Show
import com.jetpack.myapplication.data.TMDBRepository

class ShowDetailRepository(
    private val tmdbRepository: TMDBRepository = TMDBRepository(),
    private val traktService: TraktService = RetrofitClient.instance.create(TraktService::class.java)
) {
    suspend fun getShowDetail(showId: String): ShowDetail {
        // Step 1: Ask Trakt for full show details to get the correct TMDB ID
        val traktDetails: Show = traktService.getShowDetails(showId)

        val tmdbId = traktDetails.ids.tmdb
            ?: throw IllegalArgumentException("No TMDB ID found for Trakt show ID: $showId")

        // Step 2: Use TMDB ID to fetch real show details
        val response = tmdbRepository.getTvDetails(tmdbId)

        return ShowDetail(
            id = tmdbId,
            title = response.name ?: "No Title",
            overview = response.overview ?: "No Overview",
            backdropUrl = "https://image.tmdb.org/t/p/w780${response.backdropPath}",
            posterUrl = "https://image.tmdb.org/t/p/w500${response.posterPath}",
            year = response.firstAirDate?.take(4)?.toIntOrNull() ?: 2024,
            genres = response.genres?.mapNotNull { it.name } ?: emptyList(),
            rating = response.voteAverage ?: 0.0,
            seasons = response.numberOfSeasons ?: 0,
            episodes = response.numberOfEpisodes ?: 0
        )
    }
}
