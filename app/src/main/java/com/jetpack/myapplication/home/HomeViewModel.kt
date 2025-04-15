package com.jetpack.myapplication.home


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jetpack.myapplication.application.Show
import com.jetpack.myapplication.application.TrendingShow
import com.jetpack.myapplication.data.HomeRepository
import com.jetpack.myapplication.data.TMDBRepository
import com.jetpack.myapplication.home.HomeUiState
import com.jetpack.myapplication.home.PopularTab
import com.jetpack.myapplication.localDataModels.PopularShowWithPoster
import com.jetpack.myapplication.localDataModels.TrendingShowWithPoster
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: HomeRepository = HomeRepository(),
    private val tmdbRepository: TMDBRepository = TMDBRepository()
) : ViewModel() {

    private val _trendingShows = MutableStateFlow<List<TrendingShow>>(emptyList())
    val trendingShows: StateFlow<List<TrendingShow>> = _trendingShows

    private val _trendingShowsWithPoster = MutableStateFlow<List<TrendingShowWithPoster>>(emptyList())
    val trendingShowsWithPoster: StateFlow<List<TrendingShowWithPoster>> = _trendingShowsWithPoster

    private val _popularShows = MutableStateFlow<List<Show>>(emptyList())
    val popularShows: StateFlow<List<Show>> = _popularShows

    private val _popularShowsWithPoster = MutableStateFlow<List<PopularShowWithPoster>>(emptyList())
    val popularShowsWithPoster: StateFlow<List<PopularShowWithPoster>> = _popularShowsWithPoster

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        loadShows()
    }
    fun updatePopularTab(selectedTab: PopularTab) {
        _uiState.value = _uiState.value.copy(popularTab = selectedTab)
    }


    private fun loadShows() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val trending = repository.fetchTrendingShows()
                val popular = repository.fetchPopularShows()

                val trendingWithPoster = coroutineScope {
                    trending.map { trendingShow ->
                        async {
                            val posterPath = trendingShow.show.ids.tmdb?.let { tmdbId ->
                                tmdbRepository.getPosterPath(tmdbId)
                            }
                            TrendingShowWithPoster(
                                show = trendingShow.show,
                                watchers = trendingShow.watchers,
                                posterPath = posterPath
                            )
                        }
                    }.awaitAll() // Wait for all async jobs to finish
                }

                val popularWithPoster = coroutineScope {
                    popular.map { popularShow ->
                        async {
                            val posterPath = popularShow.ids.tmdb?.let { tmdbId ->
                                tmdbRepository.getPosterPath(tmdbId)
                            }
                            PopularShowWithPoster(
                                show = popularShow,
                                posterPath = posterPath
                            )
                        }
                    }.awaitAll()
                }

                _trendingShowsWithPoster.value = trendingWithPoster
                _popularShowsWithPoster.value = popularWithPoster

                _uiState.value = HomeUiState(
                    heroItem = trendingWithPoster.firstOrNull()?.let {
                        ShowItem(
                            id = it.show.ids.trakt.toString(),
                            title = it.show.title,
                            posterUrl = it.posterPath?.let { path -> "https://image.tmdb.org/t/p/w500$path" }
                        )
                    },
                    discover = trendingWithPoster,
                    popularToday = popularWithPoster,
                    popularThisWeek = popularWithPoster
                )

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}
