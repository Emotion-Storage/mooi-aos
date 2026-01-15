package com.emotionstorage.domain.model

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

data class Notification(
    val id: Long,
    val type: NotificationType,
    val title: String,
    val arrivedAt: LocalDateTime,
    val body: String = "",
    val isRead: Boolean = false,
)

sealed class NotificationType {
    object RecordSchedule : NotificationType()

    data class DailyReportArrival(
        val dailyReportId: Long
    ) : NotificationType()

    data class TimeCapsuleArrival(
        val timeCapsuleId: Long
    ) : NotificationType()

    object RecordReminder : NotificationType()
}
