package com.jetpack.myapplication.showDetails

import com.jetpack.myapplication.api.TraktService
import com.jetpack.myapplication.application.RetrofitClient
import com.jetpack.myapplication.application.Show
import com.jetpack.myapplication.data.EpisodeDao
import com.jetpack.myapplication.data.TMDBRepository

class ShowDetailRepository(
    private val tmdbRepository: TMDBRepository = TMDBRepository(),
    private val traktService: TraktService = RetrofitClient.instance.create(TraktService::class.java),
    private val episodeDao: EpisodeDao
)
 {
    suspend fun getEpisodesForSeason(tmdbId: Int, seasonNumber: Int): List<Episode> {
        val seasonDetails = tmdbRepository.getSeasonDetails(tmdbId, seasonNumber)
        val watchedIds = episodeDao.getWatchedForShow(tmdbId).map { it.episodeId }.toSet()

        return seasonDetails.episodes.map { ep ->
            Episode(
                id = ep.id,
                seasonNumber = ep.seasonNumber ?: 0,
                episodeNumber = ep.episodeNumber ?: 0,
                title = ep.name ?: "No Title",
                overview = ep.overview ?: "",
                imageUrl = ep.stillPath?.let { "https://image.tmdb.org/t/p/w500$it" },
                isWatched = watchedIds.contains(ep.id)
            )
        }
    }

    suspend fun getShowDetail(showId: String): ShowDetail {
        val traktDetails = traktService.getShowDetails(showId)
        val tmdbId = traktDetails.ids.tmdb ?: throw IllegalArgumentException("No TMDB ID found for Trakt show ID: $showId")

        val showDetails = tmdbRepository.getTvDetails(tmdbId)
        val seasonNumber = 1
        val seasonDetails = tmdbRepository.getSeasonDetails(tmdbId, seasonNumber)

        // ✅ Fetch watched episodes from DB
        val watchedIds = episodeDao.getWatchedForShow(tmdbId).map { it.episodeId }.toSet()

        val episodes = seasonDetails.episodes.map { ep ->
            Episode(
                id = ep.id,
                seasonNumber = ep.seasonNumber ?: 0,
                episodeNumber = ep.episodeNumber ?: 0,
                title = ep.name ?: "No Title",
                overview = ep.overview ?: "",
                imageUrl = ep.stillPath?.let { "https://image.tmdb.org/t/p/w500$it" },
                isWatched = watchedIds.contains(ep.id)  // ✅ apply watched status
            )
        }

        return ShowDetail(
            id = tmdbId,
            title = showDetails.name ?: "No Title",
            overview = showDetails.overview ?: "No Overview",
            backdropUrl = "https://image.tmdb.org/t/p/w780${showDetails.backdropPath}",
            posterUrl = "https://image.tmdb.org/t/p/w500${showDetails.posterPath}",
            year = showDetails.firstAirDate?.take(4)?.toIntOrNull() ?: 2024,
            genres = showDetails.genres?.mapNotNull { it.name } ?: emptyList(),
            rating = showDetails.voteAverage ?: 0.0,
            seasons = showDetails.numberOfSeasons ?: 0,
            episodes = showDetails.numberOfEpisodes ?: 0,
            episodeList = episodes,
            selectedSeason = seasonNumber
        )
    }
}
