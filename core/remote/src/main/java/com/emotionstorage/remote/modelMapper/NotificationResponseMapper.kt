package com.emotionstorage.remote.modelMapper

import com.emotionstorage.data.model.NotificationEntity
import com.emotionstorage.data.model.NotificationEntity.PageData
import com.emotionstorage.remote.response.notification.GetNotificationsResponse


internal object NotificationResponseMapper {
    fun toData(response: GetNotificationsResponse): List<NotificationEntity> =
        response.notifications.map { it ->
            NotificationEntity(
                id = it.notificationId,
                title = it.notification.title,
                body = it.notification.body,
                isRead = it.isRead,
                arrivedAt = it.arrivedAt,
                type = it.data.type,
                targetId = it.data.targetId,
                pageData =
                    PageData(
                        page = response.pagination.page,
                        hasNextPage = response.pagination.page < response.pagination.totalPage,
                    ),
            )
        }
}
