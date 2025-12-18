package com.emotionstorage.domain.useCase.notification

import com.emotionstorage.domain.repo.NotificationPermissionRepository
import javax.inject.Inject

class SetNotificationPermissionPromptedUseCase @Inject constructor(
    private val repository: NotificationPermissionRepository,
) {
    suspend operator fun invoke(value: Boolean) = repository.setPrompted(value)
}
