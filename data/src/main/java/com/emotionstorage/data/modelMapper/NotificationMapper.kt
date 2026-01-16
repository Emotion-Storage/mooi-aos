package com.emotionstorage.data.modelMapper

import com.emotionstorage.data.model.NotificationEntity
import com.emotionstorage.domain.model.Notification
import com.emotionstorage.domain.model.NotificationType

internal object NotificationMapper {
    fun toDomain(entity: NotificationEntity) =
        Notification(
            id = entity.id,
            type =
                when (entity.type) {
                    "RECORD_SCHEDULE" -> {
                        NotificationType.RecordSchedule
                    }

                    "DAILY_REPORT_ARRIVAL" -> {
                        entity.targetId?.let {
                            NotificationType.DailyReportArrival(it)
                        } ?: throw IllegalArgumentException("targetId is null")
                    }

                    "TIME_CAPSULE_ARRIVAL" -> {
                        entity.targetId?.let {
                            NotificationType.TimeCapsuleArrival(it)
                        } ?: throw IllegalArgumentException("targetId is null")
                    }

                    "RECORD_REMINDER" -> {
                        NotificationType.RecordReminder
                    }

                    else -> {
                        throw IllegalArgumentException("Invalid notification type")
                    }
                },
            arrivedAt = entity.arrivedAt,
            isRead = entity.isRead,
        )
}
