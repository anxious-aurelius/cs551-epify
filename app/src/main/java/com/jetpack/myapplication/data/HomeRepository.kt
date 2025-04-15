package com.jetpack.myapplication.data

import android.util.Log
import com.jetpack.myapplication.api.TraktService
import com.jetpack.myapplication.application.RetrofitClient
import com.jetpack.myapplication.application.Show
import com.jetpack.myapplication.application.TrendingShow



class HomeRepository {
    private val traktService = RetrofitClient.instance.create(TraktService::class.java)

    companion object {
        private const val TAG = "HomeRepositoryLog"
    }

    suspend fun fetchTrendingShows(page: Int = 1, limit: Int = 10): List<TrendingShow> {
        val trendingShows = traktService.getTrendingShows(page, limit)
        Log.d(TAG, "Fetched Trending Shows: $trendingShows")
        return trendingShows
    }

    suspend fun fetchPopularShows(page: Int = 1, limit: Int = 10): List<Show> {
        val popularShows = traktService.getPopularShows(page, limit)
        Log.d(TAG, "Fetched Popular Shows: $popularShows")
        return popularShows
    }
}
