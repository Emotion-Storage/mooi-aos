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

    fun load() =
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loading = true, error = null)
            getAttendanceUseCase().collect { s ->
                _uiState.value =
                    when (s) {
                        is DataState.Success -> {
                            _uiState.value.copy(
                                summary = s.data,
                                loading = false,
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

    fun claimToday() =
        viewModelScope.launch {
            claimAttendanceRewardUseCase().collect { s ->
                when (s) {
                    is DataState.Success -> {
                        _uiState.value =
                            _uiState.value.copy(
                                summary = s.data,
                                showDialog = false,
                            )
                    }

                    is DataState.Error -> {
                        _uiState.value =
                            _uiState.value.copy(error = s.throwable.toString())
                    }

                    is DataState.Loading -> {
                        // no-op
                    }
                }
            }
        }

    data class UiState(
        val summary: AttendanceSummary? = null,
        val showDialog: Boolean = false,
        val loading: Boolean = false,
        val error: String? = null,
    )
}
