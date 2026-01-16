package com.emotionstorage.data.dataSource.remote

import com.emotionstorage.data.model.NotificationEntity

interface NotificationRemoteDataSource {
    suspend fun getNotifications(
        page: Int,
        limit: Int,
    ): List<NotificationEntity>
}
