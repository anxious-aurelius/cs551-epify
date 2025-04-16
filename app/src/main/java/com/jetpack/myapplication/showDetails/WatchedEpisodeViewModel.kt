package com.jetpack.myapplication.showDetails
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jetpack.myapplication.data.WatchedEpisodeEntity
import com.jetpack.myapplication.data.WatchedEpisodeRepository

import kotlinx.coroutines.launch

class WatchedEpisodeViewModel(private val repo: WatchedEpisodeRepository) : ViewModel() {

    fun markAsWatched(episode: WatchedEpisodeEntity) {
        viewModelScope.launch {
            repo.markAsWatched(episode)
        }
    }

    suspend fun getWatchedEpisodes(showId: Int): List<WatchedEpisodeEntity> {
        return repo.getWatchedForShow(showId)
    }
}