package com.emotionstorage.domain.model

import java.time.DayOfWeek
import java.time.LocalTime

data class NotificationSettings(
    val appPushNotify: Boolean,
    val emotionReminderNotify: Boolean,
    val emotionReminderDays: Set<DayOfWeek>,
    val emotionReminderTime: LocalTime,
    val timeCapsuleReportNotify: Boolean,
    val marketingInfoNotify: Boolean,
)

