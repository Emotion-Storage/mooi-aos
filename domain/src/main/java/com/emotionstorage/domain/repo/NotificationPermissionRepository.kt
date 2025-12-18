package com.emotionstorage.domain.repo

import com.emotionstorage.domain.model.NotificationPermissionInfo
import com.emotionstorage.domain.model.NotificationPermissionStatus
import kotlinx.coroutines.flow.Flow

interface NotificationPermissionRepository {
    fun observeInfo(): Flow<NotificationPermissionInfo>

    suspend fun updateStatus(status: NotificationPermissionStatus)

    suspend fun setPrompted(value: Boolean)
}
