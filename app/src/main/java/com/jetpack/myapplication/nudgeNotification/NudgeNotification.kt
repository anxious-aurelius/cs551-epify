package com.jetpack.myapplication.nudgeNotification

enum class Frequency {
    DAILY, WEEKLY, MONTHLY
}

data class NudgePreferences(
    val frequency: Frequency = Frequency.DAILY,
    val hour: Int = 9,
    val minute: Int = 0
)
