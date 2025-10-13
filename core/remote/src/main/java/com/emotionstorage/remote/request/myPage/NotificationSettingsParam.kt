package com.emotionstorage.remote.request.myPage

import com.emotionstorage.domain.model.NotificationSettings
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.time.format.DateTimeFormatter

@Serializable
data class NotificationSettingsParam(
    val appPushNotify: Boolean,
    val emotionReminderNotify: Boolean,
    val emotionReminderDays: List<String>,
    val emotionReminderTime: String,
    val timeCapsuleReportNotify: Boolean,
    val marketingInfoNotify: Boolean,
)

fun NotificationSettings.toRequest(): NotificationSettingsParam =
    NotificationSettingsParam(
        appPushNotify = appPushNotify,
        emotionReminderNotify = emotionReminderNotify,
        emotionReminderDays = emotionReminderDays.map { it.name },
        emotionReminderTime = emotionReminderTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")),
        timeCapsuleReportNotify = timeCapsuleReportNotify,
        marketingInfoNotify = marketingInfoNotify,
    )

fun NotificationSettingsParam.toBody(): RequestBody = Json.encodeToString(this).toRequestBody()
