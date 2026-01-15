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
class PushNotificationViewModel @Inject constructor() : ViewModel() {
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
        title = "오늘 어떻게 보냈어요?",
        body = "오늘 있었던 일, 아무거나 들어줄게요.",
        arrivedAt = LocalDateTime.now().minusHours(1),
    ),
    Notification(
        id = 0,
        type = NotificationType.DailyReportArrival(1),
        title = "어제의 나, 리포트로 돌아왔어요",
        body = "하루의 마음 여정을 한눈에 만나보세요.",
        arrivedAt = LocalDateTime.now().minusHours(3),
    ),
    Notification(
        id = 0,
        type = NotificationType.TimeCapsuleArrival(1),
        title = "기다리던 타임캡슐 도착!",
        body = "잠들어 있던 감정이 깨어났어요.",
        arrivedAt = LocalDateTime.now().minusDays(0),
    ),
    Notification(
        id = 0,
        type = NotificationType.RecordReminder,
        title = "우리 못본지 오래된 것 같아요...",
        body = "00님의 안부가 궁금해요.",
        arrivedAt = LocalDateTime.now().minusDays(3),
    ),
)


