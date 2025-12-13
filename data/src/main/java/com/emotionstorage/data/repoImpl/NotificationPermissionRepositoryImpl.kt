package com.emotionstorage.data.repoImpl

import com.emotionstorage.data.dataSource.local.NotificationPermissionDataSource
import com.emotionstorage.domain.model.NotificationPermissionStatus
import com.emotionstorage.domain.repo.NotificationPermissionRepository
import javax.inject.Inject

class NotificationPermissionRepositoryImpl @Inject constructor(
    private val datsSource: NotificationPermissionDataSource,
) : NotificationPermissionRepository {
    override fun observeInfo() = datsSource.observeInfo()

    override suspend fun updateStatus(status: NotificationPermissionStatus) = datsSource.updateStatus(status)

    override suspend fun setPrompted(value: Boolean) = datsSource.setPrompted(value)
}
