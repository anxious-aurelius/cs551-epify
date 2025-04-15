package com.jetpack.myapplication.localDataModels

import com.jetpack.myapplication.application.Show

data class TrendingShowWithPoster(
    val show: Show,
    val watchers: Int,
    val posterPath: String?
)
