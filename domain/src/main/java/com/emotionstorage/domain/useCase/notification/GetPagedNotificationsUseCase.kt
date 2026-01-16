package com.emotionstorage.domain.useCase.notification

import androidx.paging.PagingData
import com.emotionstorage.domain.model.Notification
import com.emotionstorage.domain.repo.NotificationRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class GetPagedNotificationsUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository,
) {
    operator fun invoke(): Flow<PagingData<Notification>> =
        notificationRepository.getPagedNotifications()
}
