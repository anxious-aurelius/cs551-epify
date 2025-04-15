package com.jetpack.myapplication.showDetails
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ShowDetailViewModel(
    private val showDetailRepository: ShowDetailRepository = ShowDetailRepository()
) : ViewModel() {

    private val _showDetail = mutableStateOf<ShowDetail?>(null)
    val showDetail: State<ShowDetail?> get() = _showDetail

    fun loadShowDetails(showId: String) {
        viewModelScope.launch {
            _showDetail.value = showDetailRepository.getShowDetail(showId)
        }
    }
}
