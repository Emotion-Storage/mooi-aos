package com.emotionstorage.domain.repo

import androidx.paging.PagingData
import com.emotionstorage.domain.model.Notification
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    fun getPagedNotifications(): Flow<PagingData<Notification>>
}
