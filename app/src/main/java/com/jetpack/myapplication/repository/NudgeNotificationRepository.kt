package com.jetpack.myapplication.repository

import android.content.Context

import androidx.work.*
import com.jetpack.myapplication.datastore.*
import com.jetpack.myapplication.worker.NudgeWorker
import kotlinx.coroutines.flow.Flow


interface NudgeNotificationRepository {
    val nudgePreferencesFlow: Flow<NudgePreferences>
    suspend fun saveNudgePreferences(preferences: NudgePreferences)
    fun scheduleNudge(context: Context, preferences: NudgePreferences)
}

class NudgeNotificationRepositoryImpl(
    private val dataStoreManager: NudgeDataStoreManager
) : NudgeNotificationRepository {

    override val nudgePreferencesFlow: Flow<NudgePreferences> = dataStoreManager.preferencesFlow

    override suspend fun saveNudgePreferences(preferences: NudgePreferences) {
        dataStoreManager.savePreferences(preferences)
    }

    override fun scheduleNudge(context: Context, preferences: NudgePreferences) {
        val repeatInterval = when (preferences.frequency) {
            NudgeFrequency.DAILY -> 1L
            NudgeFrequency.WEEKLY -> 7L
            NudgeFrequency.MONTHLY -> 30L
        }

        val calendar = java.util.Calendar.getInstance().apply {
            set(java.util.Calendar.HOUR_OF_DAY, preferences.hour)
            set(java.util.Calendar.MINUTE, preferences.minute)
            set(java.util.Calendar.SECOND, 0)
            set(java.util.Calendar.MILLISECOND, 0)
        }

        var delay = calendar.timeInMillis - System.currentTimeMillis()
        if (delay < 0) {
            delay += java.util.concurrent.TimeUnit.DAYS.toMillis(1) // shift to next day
        }

        val workRequest = PeriodicWorkRequestBuilder<NudgeWorker>(
            repeatInterval, java.util.concurrent.TimeUnit.DAYS
        )
            .setInitialDelay(delay, java.util.concurrent.TimeUnit.MILLISECONDS)
            .setConstraints(
                Constraints.Builder()
                    .setRequiresBatteryNotLow(true)
                    .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                    .build()
            )
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "nudge_work",
            ExistingPeriodicWorkPolicy.REPLACE,
            workRequest
        )
    }

//    @RequiresApi(Build.VERSION_CODES.O)
//    override fun scheduleNudge(context: Context, preferences: NudgePreferences) {
//        Log.d("NudgeSchedule", "Scheduled nudge: $preferences")
//
//
//        val repeatInterval = when (preferences.frequency) {
//            NudgeFrequency.DAILY -> 1
//            NudgeFrequency.WEEKLY -> 7
//            NudgeFrequency.MONTHLY -> 30
//        }
//
//        val now = LocalDateTime.now()
//        val triggerTime = now.withHour(preferences.hour).withMinute(preferences.minute).withSecond(0)
//        val initialDelayMillis = Duration.between(now, triggerTime).toMillis().let {
//            if (it < 0) it + TimeUnit.DAYS.toMillis(1) else it
//        }
//
//        val workRequest = PeriodicWorkRequestBuilder<NudgeWorker>(
//            repeatInterval.toLong(), TimeUnit.DAYS
//        )
//            .setInitialDelay(initialDelayMillis, TimeUnit.MILLISECONDS)
//            .setConstraints(
//                Constraints.Builder()
//                    .setRequiresBatteryNotLow(true)
//                    .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
//                    .build()
//            )
//            .build()
//
//        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
//            "nudge_work",
//            ExistingPeriodicWorkPolicy.REPLACE,
//            workRequest
//        )
//    }
}
