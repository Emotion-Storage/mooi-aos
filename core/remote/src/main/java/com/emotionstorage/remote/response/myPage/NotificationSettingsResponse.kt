package com.emotionstorage.remote.response.myPage

import com.emotionstorage.domain.model.NotificationSettings
import kotlinx.serialization.Serializable
import java.time.DayOfWeek
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Serializable
data class NotificationSettingsResponse(
    val appPushNotify: Boolean,
    val emotionReminderNotify: Boolean,
    val emotionReminderDays: List<String>,
    val emotionReminderTime: String,
    val timeCapsuleReportNotify: Boolean,
    val marketingInfoNotify: Boolean,
)

fun NotificationSettingsResponse.toDomain(): NotificationSettings =
    NotificationSettings(
        appPushNotify = appPushNotify,
        emotionReminderNotify = emotionReminderNotify,
        emotionReminderDays = emotionReminderDays.map { DayOfWeek.valueOf(it) }.toSet(),
        emotionReminderTime = LocalTime.parse(emotionReminderTime, DateTimeFormatter.ofPattern("HH:mm:ss")),
        timeCapsuleReportNotify = timeCapsuleReportNotify,
        marketingInfoNotify = marketingInfoNotify,
    )
