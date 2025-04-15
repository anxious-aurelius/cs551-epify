package com.jetpack.myapplication.localDataModels

import com.jetpack.myapplication.application.Show

data class PopularShowWithPoster(
    val show: Show,
    val posterPath: String?
)
