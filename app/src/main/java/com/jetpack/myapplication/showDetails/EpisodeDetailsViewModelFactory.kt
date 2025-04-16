package com.jetpack.myapplication.showDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jetpack.myapplication.data.EpisodeDao

class EpisodeDetailsViewModelFactory(
    private val episodeDao: EpisodeDao,
    private val episodeId: Int
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EpisodeDetailsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EpisodeDetailsViewModel(episodeDao, episodeId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
