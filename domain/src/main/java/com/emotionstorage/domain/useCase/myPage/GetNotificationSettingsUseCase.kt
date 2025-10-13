package com.emotionstorage.domain.useCase.myPage

import com.emotionstorage.domain.repo.NotificationSettingRepository
import javax.inject.Inject

class GetNotificationSettingsUseCase @Inject constructor(
    private val notificationSettingRepository: NotificationSettingRepository,
) {
    suspend operator fun invoke() = notificationSettingRepository.getNotificationSettings()
}
