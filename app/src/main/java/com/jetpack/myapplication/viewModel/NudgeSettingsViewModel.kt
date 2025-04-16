package com.jetpack.myapplication.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jetpack.myapplication.datastore.NudgeFrequency
import com.jetpack.myapplication.datastore.NudgePreferences
import com.jetpack.myapplication.repository.NudgeNotificationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NudgeSettingsViewModel(
    private val repository: NudgeNotificationRepository,
    private val context: Context
) : ViewModel() {

    private val _nudgePreferences = MutableStateFlow(NudgePreferences())
    val nudgePreferences: StateFlow<NudgePreferences> = _nudgePreferences.asStateFlow()

    init {
        viewModelScope.launch {
            repository.nudgePreferencesFlow.collectLatest {
                _nudgePreferences.value = it
            }
        }
    }

    fun updateFrequency(frequency: NudgeFrequency) {
        val updated = _nudgePreferences.value.copy(frequency = frequency)
        updatePreferences(updated)
    }

    fun updateTime(hour: Int, minute: Int) {
        val updated = _nudgePreferences.value.copy(hour = hour, minute = minute)
        updatePreferences(updated)
    }

    private fun updatePreferences(preferences: NudgePreferences) {
        viewModelScope.launch {
            repository.saveNudgePreferences(preferences)
            repository.scheduleNudge(context, preferences)
        }
    }
}
