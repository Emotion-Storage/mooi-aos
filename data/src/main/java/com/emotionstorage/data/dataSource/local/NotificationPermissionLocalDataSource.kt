package com.emotionstorage.data.dataSource.local

import com.emotionstorage.domain.model.NotificationPermissionInfo
import com.emotionstorage.domain.model.NotificationPermissionStatus

interface NotificationPermissionLocalDataSource {
    fun observeInfo(): kotlinx.coroutines.flow.Flow<NotificationPermissionInfo>

    suspend fun updateStatus(status: NotificationPermissionStatus)

    suspend fun setPrompted(value: Boolean)
}
