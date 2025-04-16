package com.jetpack.myapplication.showDetails

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jetpack.myapplication.data.EpisodeDao
import com.jetpack.myapplication.data.WatchedEpisodeEntity
import kotlinx.coroutines.launch

class EpisodeDetailsViewModel(private val episodeDao: EpisodeDao, private val episodeId: Int) : ViewModel() {
    var episode by mutableStateOf<WatchedEpisodeEntity?>(null)
        private set

    init {
        viewModelScope.launch {
            episode = episodeDao.getEpisodeById(episodeId)
        }
    }

    fun toggleWatchedStatus() {
        viewModelScope.launch {
            episodeDao.toggleWatchedStatus(episodeId)
            episode = episodeDao.getEpisodeById(episodeId)
        }
    }
}

