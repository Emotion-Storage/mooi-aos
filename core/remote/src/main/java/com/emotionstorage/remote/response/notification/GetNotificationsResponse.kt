package com.emotionstorage.remote.response.notification

import com.emotionstorage.common.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class GetNotificationsResponse(
    val pagination: PaginationInfo,
    val totalNotifications: Int,
    val notifications: List<Notification>,
) {
    @Serializable
    data class PaginationInfo(
        val page: Int,
        val limit: Int,
        val totalPage: Int,
    )

    @Serializable
    data class Notification(
        val notificationId: Long,
        val notification: NotificationInfo,
        val isRead: Boolean,
        @Serializable(with = LocalDateTimeSerializer::class)
        val arrivedAt: LocalDateTime,
        val data: NotificationData,
    ) {
        @Serializable
        data class NotificationInfo(
            val title: String,
            val body: String,
        )

        @Serializable
        data class NotificationData(
            val type: String,
            val targetId: Long? = null
        )
    }
}
