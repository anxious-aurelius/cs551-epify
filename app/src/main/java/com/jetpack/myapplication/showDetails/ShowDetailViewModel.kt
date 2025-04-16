package com.jetpack.myapplication.showDetails

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ShowDetailViewModel(
    private val showDetailRepository: ShowDetailRepository
) : ViewModel() {

    private val _showDetail = mutableStateOf<ShowDetail?>(null)
    val showDetail: State<ShowDetail?> get() = _showDetail

    fun loadShowDetails(showId: String) {
        viewModelScope.launch {
            _showDetail.value = showDetailRepository.getShowDetail(showId)
        }
    }

    fun loadSeasonEpisodes(seasonNumber: Int) {
        viewModelScope.launch {
            val currentDetail = _showDetail.value ?: return@launch
            val episodes = showDetailRepository.getEpisodesForSeason(currentDetail.id, seasonNumber)

            _showDetail.value = currentDetail.copy(
                episodeList = episodes,
                selectedSeason = seasonNumber
            )
        }
    }
}
