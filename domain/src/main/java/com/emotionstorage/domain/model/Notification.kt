package com.emotionstorage.domain.model

import java.time.LocalDateTime

data class Notification(
    val id: Long,
    val type: NotificationType,
    val arrivedAt: LocalDateTime,
    val isRead: Boolean = false,
)

sealed class NotificationType {
    object RecordSchedule : NotificationType()

    data class DailyReportArrival(
        val dailyReportId: Long,
    ) : NotificationType()

    data class TimeCapsuleArrival(
        val timeCapsuleId: Long,
    ) : NotificationType()

    object RecordReminder : NotificationType()

    data class UnKnown(
        val data: Any? = null
    ): NotificationType()
}
