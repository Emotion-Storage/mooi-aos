package com.emotionstorage.domain.useCase.notification

import com.emotionstorage.domain.repo.NotificationPermissionRepository
import javax.inject.Inject

class ObserveNotificationPermissionInfoUseCase @Inject constructor(
    private val repository: NotificationPermissionRepository,
) {
    operator fun invoke() = repository.observeInfo()
}
