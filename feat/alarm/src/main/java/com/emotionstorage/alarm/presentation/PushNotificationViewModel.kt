package com.emotionstorage.alarm.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emotionstorage.domain.model.Notification
import com.emotionstorage.domain.model.NotificationType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class PushNotificationViewModel @Inject constructor(

) : ViewModel() {
    private val _state = MutableStateFlow<List<Notification>>(emptyList())
    val state: StateFlow<List<Notification>> = _state

    init {
        // TODO : Mock Data 추후 삭제
        loadData(useMock = true)
    }

    fun loadData(useMock: Boolean) {
        viewModelScope.launch {
            _state.value = if (useMock) mockItems() else emptyList()
        }
    }
}

private fun mockItems() = listOf(
    Notification(
        id = 0,
        type = NotificationType.RecordSchedule,
        arrivedAt = LocalDateTime.now().minusHours(1),
    ),
    Notification(
        id = 0,
        type = NotificationType.DailyReportArrival(1),
        arrivedAt = LocalDateTime.now().minusHours(3),
    ),
    Notification(
        id = 0,
        type = NotificationType.TimeCapsuleArrival(1),
        arrivedAt = LocalDateTime.now().minusDays(0),
    ),
    Notification(
        id = 0,
        type = NotificationType.RecordReminder,
        arrivedAt = LocalDateTime.now().minusDays(3),
    ),
)


