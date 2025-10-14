package com.emotionstorage.alarm.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PushNotificationViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow<List<PushNotificationState>>(emptyList())
    val state: StateFlow<List<PushNotificationState>> = _state

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

private fun mockItems() =
    listOf(
        PushNotificationState("1", "어제의 일일리포트가 업데이트 되었습니다.", "0시간 전"),
        PushNotificationState("2", "새로운 타임캡슐이 도착했어요!", "22시간 전"),
        PushNotificationState("3", "어제의 일일리포트가 업데이트 되었습니다.", "1일 전"),
        PushNotificationState("4", "새로운 타임캡슐이 도착했어요!", "21일 전"),
    )

data class PushNotificationState(
    val id: String = "0",
    val title: String = "",
    val timeText: String = "",
)
