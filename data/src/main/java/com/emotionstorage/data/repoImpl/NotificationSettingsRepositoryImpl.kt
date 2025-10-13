package com.emotionstorage.data.repoImpl

import com.emotionstorage.data.dataSource.NotificationSettingRemoteDataSource
import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.model.NotificationSettings
import com.emotionstorage.domain.repo.NotificationSettingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NotificationSettingRepositoryImpl @Inject constructor(
    private val dataSource: NotificationSettingRemoteDataSource
) : NotificationSettingRepository {
    override suspend fun getNotificationSettings(): Flow<DataState<NotificationSettings>> = flow {
        emit(dataSource.getNotificationSettings())
    }

    override suspend fun updateNotificationSettings(notificationSettings: NotificationSettings): Boolean {
        return dataSource.updateNotificationSettings(notificationSettings)
    }

}

