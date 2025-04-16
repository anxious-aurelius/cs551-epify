package com.jetpack.myapplication.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.jetpack.myapplication.NudgePreferencesProto
import com.jetpack.myapplication.NudgePreferencesProto.Frequency
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class NudgePreferences(
    val frequency: NudgeFrequency = NudgeFrequency.DAILY,
    val hour: Int = 9,
    val minute: Int = 0
)

enum class NudgeFrequency {
    DAILY, WEEKLY, MONTHLY
}

private val Context.nudgePreferencesDataStore: DataStore<NudgePreferencesProto> by dataStore(
    fileName = "nudge_preferences.pb",
    serializer = NudgePreferencesSerializer
)

class NudgeDataStoreManager(private val context: Context) {

    private val dataStore = context.nudgePreferencesDataStore

    val preferencesFlow: Flow<NudgePreferences> = dataStore.data.map { proto ->
        NudgePreferences(
            frequency = when (proto.frequency) {
                Frequency.DAILY -> NudgeFrequency.DAILY
                Frequency.WEEKLY -> NudgeFrequency.WEEKLY
                Frequency.MONTHLY -> NudgeFrequency.MONTHLY
                else -> NudgeFrequency.DAILY // fallback
            },
            hour = proto.hour,
            minute = proto.minute
        )
    }

    suspend fun savePreferences(preferences: NudgePreferences) {
        dataStore.updateData { current ->
            current.toBuilder()
                .setFrequency(
                    when (preferences.frequency) {
                        NudgeFrequency.DAILY -> Frequency.DAILY
                        NudgeFrequency.WEEKLY -> Frequency.WEEKLY
                        NudgeFrequency.MONTHLY -> Frequency.MONTHLY
                    }
                )
                .setHour(preferences.hour)
                .setMinute(preferences.minute)
                .build()
        }
    }
}
