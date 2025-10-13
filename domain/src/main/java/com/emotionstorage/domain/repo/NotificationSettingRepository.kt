package com.emotionstorage.domain.repo

import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.model.NotificationSettings
import kotlinx.coroutines.flow.Flow

interface NotificationSettingRepository {
    suspend fun getNotificationSettings(): Flow<DataState<NotificationSettings>>

    suspend fun updateNotificationSettings(notificationSettings: NotificationSettings): DataState<Unit>
}
