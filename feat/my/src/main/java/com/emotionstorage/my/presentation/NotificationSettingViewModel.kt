package com.emotionstorage.my.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.model.NotificationSettings
import com.emotionstorage.domain.useCase.myPage.GetNotificationSettingsUseCase
import com.emotionstorage.domain.useCase.myPage.UpdateNotificationSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class NotificationSettingViewModel @Inject constructor(
    private val getNotificationSettingsUseCase: GetNotificationSettingsUseCase,
    private val updateNotificationSettingsUseCase: UpdateNotificationSettingsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(NotificationSettingState())
    val state: StateFlow<NotificationSettingState> = _state

    init {
        viewModelScope.launch {
            getNotificationSettingsUseCase().collect { dataState ->
                if (dataState is DataState.Success) {
                    val settings = dataState.data
                    _state.value = NotificationSettingState(
                        appPushNotify = settings.appPushNotify,
                        emotionReminderNotify = settings.emotionReminderNotify,
                        emotionReminderDays = settings.emotionReminderDays,
                        emotionReminderTime = settings.emotionReminderTime,
                        timeCapsuleReportNotify = settings.timeCapsuleReportNotify,
                        marketingInfoNotify = settings.marketingInfoNotify
                    )
                }
            }
        }
    }

    fun setAppPush(on: Boolean) = _state.update { it.copy(appPushNotify = on) }
    fun setEmotionReminder(on: Boolean) = _state.update {
        if (on) {
            it.copy(emotionReminderNotify = true)
        } else {
            it.copy(
                emotionReminderNotify = false,
                emotionReminderDays = emptySet(),
                emotionReminderTime = LocalTime.of(21, 0)
            )
        }
    }

    fun setTimeCapsule(on: Boolean) = _state.update { it.copy(timeCapsuleReportNotify = on) }
    fun setMarketing(on: Boolean) = _state.update { it.copy(marketingInfoNotify = on) }
    fun setTime(time: LocalTime) = _state.update { it.copy(emotionReminderTime = time) }
    fun toggleDay(day: DayOfWeek) = _state.update {
        val days = it.emotionReminderDays.toMutableSet()
        if (!days.add(day)) days.remove(day)
        it.copy(emotionReminderDays = days)
    }

    fun save() {
        viewModelScope.launch {
            updateNotificationSettingsUseCase(
                NotificationSettings(
                    appPushNotify = state.value.appPushNotify,
                    emotionReminderNotify = state.value.emotionReminderNotify,
                    emotionReminderDays = state.value.emotionReminderDays,
                    emotionReminderTime = state.value.emotionReminderTime,
                    timeCapsuleReportNotify = state.value.timeCapsuleReportNotify,
                    marketingInfoNotify = state.value.marketingInfoNotify
                )
            )
        }
    }
}

data class NotificationSettingState(
    val appPushNotify: Boolean = false,
    val emotionReminderNotify: Boolean = false,
    val emotionReminderDays: Set<DayOfWeek> = emptySet(),
    val emotionReminderTime: LocalTime = LocalTime.of(21, 0),
    val timeCapsuleReportNotify: Boolean = false,
    val marketingInfoNotify: Boolean = false,
)
