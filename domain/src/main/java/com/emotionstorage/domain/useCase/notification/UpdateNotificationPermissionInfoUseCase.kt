package com.emotionstorage.domain.useCase.notification

import com.emotionstorage.domain.model.NotificationPermissionStatus
import com.emotionstorage.domain.repo.NotificationPermissionRepository
import javax.inject.Inject

class UpdateNotificationPermissionInfoUseCase @Inject constructor(
    private val repository: NotificationPermissionRepository,
) {
    suspend operator fun invoke(status: NotificationPermissionStatus) = repository.updateStatus(status)
}
