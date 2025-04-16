package com.jetpack.myapplication.showDetails


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ShowDetailViewModelFactory(
    private val repository: ShowDetailRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShowDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ShowDetailViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}