package com.emotionstorage.presentation.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emotionstorage.domain.model.NotificationPermissionInfo
import com.emotionstorage.domain.model.NotificationPermissionStatus
import com.emotionstorage.domain.useCase.notification.ObserveNotificationPermissionInfoUseCase
import com.emotionstorage.domain.useCase.notification.SetNotificationPermissionPromptedUseCase
import com.emotionstorage.domain.useCase.notification.UpdateNotificationPermissionInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationPermissionGateViewModel @Inject constructor(
    observeInfo: ObserveNotificationPermissionInfoUseCase,
    private val updateStatus: UpdateNotificationPermissionInfoUseCase,
    private val setPrompted: SetNotificationPermissionPromptedUseCase,
) : ViewModel() {
    val info =
        observeInfo().stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            NotificationPermissionInfo(
                status = NotificationPermissionStatus.Unknown,
                hasPrompted = false,
            ),
        )

    fun syncFromSystem(canPostNotifications: Boolean) {
        viewModelScope.launch {
            val nextStatus = if (canPostNotifications) {
                NotificationPermissionStatus.Granted
            } else {
                when (info.value.status) {
                    NotificationPermissionStatus.DeniedAlways -> NotificationPermissionStatus.DeniedAlways
                    else -> NotificationPermissionStatus.Denied
                }
            }
            updateStatus(nextStatus)
        }
    }

    fun markPrompted() {
        viewModelScope.launch { setPrompted(true) }
    }

    fun onPermissionResult(
        granted: Boolean,
        showRationale: Boolean,
    ) {
        val status =
            when {
                granted -> NotificationPermissionStatus.Granted
                !showRationale -> NotificationPermissionStatus.DeniedAlways
                else -> NotificationPermissionStatus.Denied
            }
        viewModelScope.launch {
            updateStatus(status)
            setPrompted(true)
        }
    }
}
