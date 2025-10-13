package com.emotionstorage.domain.useCase.myPage

import com.emotionstorage.domain.model.NotificationSettings
import com.emotionstorage.domain.repo.NotificationSettingRepository
import javax.inject.Inject

class UpdateNotificationSettingsUseCase @Inject constructor(
    private val notificationSettingRepository: NotificationSettingRepository,
) {
    suspend operator fun invoke(notificationSettings: NotificationSettings) =
        notificationSettingRepository.updateNotificationSettings(notificationSettings)
}
