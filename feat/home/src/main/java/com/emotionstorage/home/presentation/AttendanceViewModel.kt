package com.emotionstorage.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.model.AttendanceSummary
import com.emotionstorage.domain.useCase.home.ClaimAttendanceRewardUseCase
import com.emotionstorage.domain.useCase.home.GetAttendanceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AttendanceViewModel @Inject constructor(
    private val getAttendanceUseCase: GetAttendanceUseCase,
    private val claimAttendanceRewardUseCase: ClaimAttendanceRewardUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    private val kst = java.time.ZoneId.of("Asia/Seoul")
    private val iso =
        java
            .time
            .format
            .DateTimeFormatter
            .ISO_DATE

    private fun todayKst(): String =
        java
            .time
            .LocalDate
            .now(kst)
            .format(iso)

    fun onAction(action: AttendanceAction) {
        when (action) {
            AttendanceAction.Init -> load()
            AttendanceAction.ConfirmReward -> onConfirmReward()
            AttendanceAction.ConfirmDayChanged -> confirmDayChangedAlert()
        }
    }

    private fun load() =
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loading = true, error = null, isClaiming = false)
            getAttendanceUseCase().collect { s ->
                _uiState.value =
                    when (s) {
                        is DataState.Success -> {
                            val today = todayKst()
                            _uiState.value.copy(
                                summary = s.data,
                                loading = false,
                                showDialog = s.data.canClaimToday,
                                dialogBaseDate = if (s.data.canClaimToday) today else null,
                                showDayChangedAlert = false,
                            )
                        }

                        is DataState.Error -> {
                            _uiState.value.copy(
                                error = s.throwable.toString(),
                                loading = false,
                            )
                        }

                        is DataState.Loading -> {
                            _uiState.value.copy(loading = true)
                        }
                    }
            }
        }

    private fun claimToday() =
        viewModelScope.launch {
            val currentState = _uiState.value

            if (currentState.isClaiming) return@launch

            val canClaim = currentState.summary?.canClaimToday == true
            if (!canClaim) {
                _uiState.value = currentState.copy(showDialog = false)
                return@launch
            }

            _uiState.value = currentState.copy(isClaiming = true, showDialog = false, error = null)

            claimAttendanceRewardUseCase().collect { s ->
                when (s) {
                    is DataState.Success -> {
                        _uiState.value =
                            _uiState.value.copy(
                                summary = s.data,
                                showDialog = false,
                                isClaiming = false,
                            )
                    }

                    is DataState.Error -> {
                        _uiState.value =
                            _uiState.value.copy(
                                error = s.throwable.toString(),
                                isClaiming = false,
                            )
                    }

                    is DataState.Loading -> {
                        // no-op
                    }
                }
            }
        }

    private fun onConfirmReward() =
        viewModelScope.launch {
            val currentState = _uiState.value
            val now = todayKst()
            val baseDate = currentState.dialogBaseDate

            if (baseDate == null) {
                load()
                return@launch
            }

            if (now != baseDate) {
                _uiState.value =
                    currentState.copy(
                        showDialog = false,
                        showDayChangedAlert = true,
                        dialogBaseDate = now,
                    )
                return@launch
            }

            claimToday()
        }

    private fun confirmDayChangedAlert() =
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(showDayChangedAlert = false)
            load()
        }

}

data class UiState(
    val summary: AttendanceSummary? = null,
    val showDialog: Boolean = false,
    val isClaiming: Boolean = false,
    val loading: Boolean = false,
    val error: String? = null,
    val dialogBaseDate: String? = null,
    val showDayChangedAlert: Boolean = false,
)

sealed interface AttendanceAction {
    data object Init : AttendanceAction
    data object ConfirmReward : AttendanceAction
    data object ConfirmDayChanged : AttendanceAction
}
