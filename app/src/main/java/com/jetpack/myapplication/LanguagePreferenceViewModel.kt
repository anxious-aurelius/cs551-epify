package com.jetpack.myapplication

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class LanguagePreferenceViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = LanguagePreferenceRepository(application)

    // Expose the language as a Flow
    val language = repository.languageFlow

    // Update language preference
    fun setLanguage(newLanguage: String) {
        viewModelScope.launch {
            repository.setLanguage(newLanguage)
        }
    }
}
