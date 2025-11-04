package com.emotionstorage.data.dataSource.remote

import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.model.NotificationSettings

interface NotificationSettingRemoteDataSource {
    suspend fun getNotificationSettings(): DataState<NotificationSettings>

    suspend fun updateNotificationSettings(notificationSettings: NotificationSettings): DataState<Unit>
}
