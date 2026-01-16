package com.emotionstorage.alarm.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.model.Notification
import com.emotionstorage.domain.useCase.dailyReport.GetDailyReportByIdUseCase
import com.emotionstorage.domain.useCase.notification.GetPagedNotificationsUseCase
import com.emotionstorage.domain.useCase.timeCapsule.GetTimeCapsuleByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

data class PushNotificationState(
    val isLoading: Boolean = true,
)

sealed class PushNotificationSideEffect {
    data class GetDailyReportDetailSuccess(
        val id: Long,
    ) : PushNotificationSideEffect()

    data class GetTimeCapsuleDetailSuccess(
        val id: Long,
    ) : PushNotificationSideEffect()

    object GetDetailError : PushNotificationSideEffect()
}

sealed class PushNotificationAction {
    data class GetDailyReportDetail(
        val id: Long,
    ) : PushNotificationAction()

    data class GetTimeCapsuleDetail(
        val id: Long,
    ) : PushNotificationAction()
}

@HiltViewModel
class PushNotificationViewModel @Inject constructor(
    private val getPagedNotifications: GetPagedNotificationsUseCase,
    private val getDailyReportById: GetDailyReportByIdUseCase,
    private val getTimeCapsuleById: GetTimeCapsuleByIdUseCase,
) : ViewModel(),
    ContainerHost<PushNotificationState, PushNotificationSideEffect> {
    override val container = container<PushNotificationState, PushNotificationSideEffect>(PushNotificationState())

    val notifications: Flow<PagingData<Notification>> =
        getPagedNotifications().cachedIn(viewModelScope)

    fun onAction(action: PushNotificationAction) {
        when (action) {
            is PushNotificationAction.GetDailyReportDetail -> {
                handleGetDailyReportDetail(action.id)
            }

            is PushNotificationAction.GetTimeCapsuleDetail -> {
                handleGetTimeCapsuleDetail(action.id)
            }
        }
    }

    private fun handleGetDailyReportDetail(id: Long) =
        intent {
            reduce { state.copy(isLoading = true) }
            getDailyReportById(id).handle(
                onSuccess = {
                    reduce { state.copy(isLoading = false) }
                    postSideEffect(PushNotificationSideEffect.GetDailyReportDetailSuccess(it.id))
                },
                onError = { throwable, code, data ->
                    reduce { state.copy(isLoading = false) }
                    postSideEffect(PushNotificationSideEffect.GetDetailError)
                },
            )
        }

    private fun handleGetTimeCapsuleDetail(id: Long) =
        intent {
            reduce { state.copy(isLoading = true) }
            getTimeCapsuleById(id).collect {
                if (it is DataState.Success) {
                    reduce { state.copy(isLoading = false) }
                    postSideEffect(PushNotificationSideEffect.GetTimeCapsuleDetailSuccess(it.data.id))
                } else if (it is DataState.Error) {
                    reduce { state.copy(isLoading = false) }
                    postSideEffect(PushNotificationSideEffect.GetDetailError)
                }
            }
        }
}
