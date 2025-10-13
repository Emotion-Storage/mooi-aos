package com.emotionstorage.domain.model

import java.time.DayOfWeek
import java.time.LocalDate

data class NotificationSettings(
    val appPushNotify: Boolean,
    val emotionReminderNotify: Boolean,
    val emotionReinderDays: Set<DayOfWeek>,
    val emotionReminderTime: LocalDate,
    val timeCapsuleReportNotify: Boolean,
    val marketingInfoNotify: Boolean,
)

