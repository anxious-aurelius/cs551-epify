package com.jetpack.myapplication.showDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jetpack.myapplication.data.WatchedEpisodeRepository

class WatchedEpisodeViewModelFactory(
    private val repo: WatchedEpisodeRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return WatchedEpisodeViewModel(repo) as T
    }
}
