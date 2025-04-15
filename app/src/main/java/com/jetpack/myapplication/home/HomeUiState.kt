package com.jetpack.myapplication.home

import com.jetpack.myapplication.application.Show
import com.jetpack.myapplication.application.TrendingShow
import com.jetpack.myapplication.localDataModels.PopularShowWithPoster
import com.jetpack.myapplication.localDataModels.TrendingShowWithPoster


data class ShowItem(
    val id: String?,
    val title: String?,
    val posterUrl: String?
)

enum class PopularTab { TODAY, THIS_WEEK }

data class HomeUiState(
    val heroItem: ShowItem? = null,
    val popularTab: PopularTab = PopularTab.TODAY,
    val popularToday: List<PopularShowWithPoster> = emptyList(),
    val popularThisWeek: List<PopularShowWithPoster> = emptyList(),
    val resumeWatching: List<Show> = emptyList(),
    val discover: List<TrendingShowWithPoster> = emptyList(),
    val upcoming: List<Show> = emptyList()
)
