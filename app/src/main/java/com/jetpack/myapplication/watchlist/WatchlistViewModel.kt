package com.jetpack.myapplication.watchlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jetpack.myapplication.data.WatchlistEntity
import com.jetpack.myapplication.data.WatchlistRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class WatchlistViewModel(private val repository: WatchlistRepository) : ViewModel() {

    // Collect and expose the watchlist as a StateFlow
    val watchlist: StateFlow<List<WatchlistEntity>> =
        repository.getWatchlist()
            .distinctUntilChanged()
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun addItem(item: WatchlistEntity) {
        viewModelScope.launch {
            repository.addItem(item)
        }
    }

    fun removeItem(item: WatchlistEntity) {
        viewModelScope.launch {
            repository.removeItem(item)
        }
    }
}
