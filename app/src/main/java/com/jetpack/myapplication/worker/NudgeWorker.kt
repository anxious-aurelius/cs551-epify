package com.jetpack.myapplication.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.jetpack.myapplication.R

class NudgeWorker(
    context: Context,
    params: WorkerParameters
) : Worker(context, params) {

    override fun doWork(): Result {
        Log.d("NudgeWorker", "🚀 NudgeWorker triggered!")
        sendNudgeNotification()
        return Result.success()
    }

    private fun sendNudgeNotification() {
        val channelId = "nudge_channel_id"
        val channelName = "Nudge Notifications"
        val manager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            manager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(android.R.drawable.ic_popup_reminder) // ✅ Safe built-in icon
            .setContentTitle("⏰ Nudge Reminder")
            .setContentText("Don't forget to update your watchlist!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        Log.d("NudgeWorker", "📢 Sending nudge notification")
        manager.notify(1001, notification)
        Log.d("NudgeWorker", "✅ notify() called")
    }
}
