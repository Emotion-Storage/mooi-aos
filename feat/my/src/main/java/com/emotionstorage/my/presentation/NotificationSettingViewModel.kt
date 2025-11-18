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
    private val getNotificationSettings: GetNotificationSettingsUseCase,
    private val updateNotificationSettings: UpdateNotificationSettingsUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(NotificationSettingState())
    val state: StateFlow<NotificationSettingState> = _state

    init {
        viewModelScope.launch {
            getNotificationSettings().collect { dataState ->
                if (dataState is DataState.Success) {
                    val settings = dataState.data
                    _state.value =
                        NotificationSettingState(
                            isLoading = false,
                            appPushNotify = settings.appPushNotify,
                            emotionReminderNotify = settings.emotionReminderNotify,
                            emotionReminderDays = settings.emotionReminderDays,
                            emotionReminderTime = settings.emotionReminderTime,
                            timeCapsuleReportNotify = settings.timeCapsuleReportNotify,
                            marketingInfoNotify = settings.marketingInfoNotify,
                        )
                }
            }
        }
    }

    fun setAppPush(isOn: Boolean) = updateSettings(appPushNotify = isOn)

    fun setEmotionReminder(on: Boolean) {
        if (on) {
            updateSettings(
                emotionReminderNotify = true,
                emotionReminderDays = DayOfWeek.entries.toSet(),
                emotionReminderTime = LocalTime.of(21, 0),
            )
        } else {
            updateSettings(
                emotionReminderNotify = false,
                emotionReminderDays = emptySet(),
                emotionReminderTime = LocalTime.of(21, 0),
            )
        }
    }

    fun setTimeCapsule(on: Boolean) = updateSettings(timeCapsuleReportNotify = on)

    fun setMarketing(on: Boolean) = updateSettings(marketingInfoNotify = on)

    fun setTime(time: LocalTime) = updateSettings(emotionReminderTime = time)

    fun toggleDay(day: DayOfWeek) {
        val days = state.value.emotionReminderDays.toMutableSet()
        if (!days.add(day)) days.remove(day)
        updateSettings(emotionReminderDays = days)
    }

    private fun updateSettings(
        appPushNotify: Boolean? = null,
        emotionReminderNotify: Boolean? = null,
        emotionReminderDays: Set<DayOfWeek>? = null,
        emotionReminderTime: LocalTime? = null,
        timeCapsuleReportNotify: Boolean? = null,
        marketingInfoNotify: Boolean? = null,
    ) {
        _state.update { it.copy(isLoading = true) }

        val newState =
            NotificationSettingState(
                isLoading = false,
                appPushNotify = appPushNotify ?: state.value.appPushNotify,
                emotionReminderNotify = emotionReminderNotify ?: state.value.emotionReminderNotify,
                emotionReminderDays = emotionReminderDays ?: state.value.emotionReminderDays,
                emotionReminderTime = emotionReminderTime ?: state.value.emotionReminderTime,
                timeCapsuleReportNotify = timeCapsuleReportNotify ?: state.value.timeCapsuleReportNotify,
                marketingInfoNotify = marketingInfoNotify ?: state.value.marketingInfoNotify,
            )
        viewModelScope
            .launch {
                updateNotificationSettings(
                    NotificationSettings(
                        appPushNotify = newState.appPushNotify,
                        emotionReminderNotify = newState.emotionReminderNotify,
                        emotionReminderDays = newState.emotionReminderDays,
                        emotionReminderTime = newState.emotionReminderTime,
                        timeCapsuleReportNotify = newState.timeCapsuleReportNotify,
                        marketingInfoNotify = newState.marketingInfoNotify,
                    ),
                )
            }.invokeOnCompletion {
                _state.update { newState }
            }
    }
}

data class NotificationSettingState(
    val isLoading: Boolean = false,
    val appPushNotify: Boolean = false,
    val emotionReminderNotify: Boolean = false,
    val emotionReminderDays: Set<DayOfWeek> = emptySet(),
    val emotionReminderTime: LocalTime = LocalTime.of(21, 0),
    val timeCapsuleReportNotify: Boolean = false,
    val marketingInfoNotify: Boolean = false,
)
