package com.emotionstorage.home.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.model.AttendanceSummary
import com.emotionstorage.domain.useCase.home.ClaimAttendanceRewardUseCase
import com.emotionstorage.domain.useCase.home.GetAttendanceUseCase
import com.emotionstorage.data.dataSource.local.AttendanceLocalDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AttendanceViewModel @Inject constructor(
    private val getAttendanceUseCase: GetAttendanceUseCase,
    private val claimAttendanceRewardUseCase: ClaimAttendanceRewardUseCase,
    private val local: AttendanceLocalDataSource,
) : ViewModel() {
    // 테스트용
    var uiState by mutableStateOf(UiState())
        private set

    fun load() =
        viewModelScope.launch {
            uiState = uiState.copy(loading = true, error = null)
            getAttendanceUseCase().collect { s ->
                when (s) {
                    is DataState.Success -> {
                        uiState = uiState.copy(summary = s.data, loading = false)
                    }

                    is DataState.Error -> {
                        uiState =
                            uiState.copy(error = s.throwable.toString(), loading = false)
                    }

                    is DataState.Loading -> {
                        uiState = uiState.copy(loading = true)
                    }
                }
            }
        }

    fun tryShowOnce() =
        viewModelScope.launch {
            val today = LocalDate.now()
            val alreadyShown = local.wasDialogShown(today).first()
            val canClaim = uiState.summary?.canClaimToday == true
            if (!alreadyShown && canClaim) {
                uiState = uiState.copy(showDialog = true)
                local.setDialogShown(today)
            }
        }

    fun claimToday() =
        viewModelScope.launch {
            claimAttendanceRewardUseCase().collect { s ->
                when (s) {
                    is DataState.Success -> {
                        uiState =
                            uiState.copy(summary = s.data, showDialog = false)
                    }

                    is DataState.Error -> {
                        uiState = uiState.copy(error = s.throwable.toString())
                    }

                    is DataState.Loading -> {
                        // no-op
                    }
                }
            }
        }

    fun dismiss() {
        uiState = uiState.copy(showDialog = false)
    }

    data class UiState(
        val summary: AttendanceSummary? = null,
        val showDialog: Boolean = false,
        val loading: Boolean = false,
        val error: String? = null,
    )
}
