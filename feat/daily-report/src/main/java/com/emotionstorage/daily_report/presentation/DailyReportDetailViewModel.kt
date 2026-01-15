package com.emotionstorage.daily_report.presentation

import androidx.lifecycle.ViewModel
import com.emotionstorage.domain.model.DailyReport
import com.emotionstorage.domain.useCase.dailyReport.GetDailyReportByIdUseCase
import com.emotionstorage.domain.useCase.dailyReport.OpenDailyReportUseCase
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

data class DailyReportDetailState(
    val isLoading: Boolean = true,
    val dailyReport: DailyReport? = null,
)

sealed class DailyReportDetailAction {
    data class Init(
        val id: Long,
    ) : DailyReportDetailAction()
}

sealed class DailyReportDetailSideEffect {
    object ShowDailyReportError : DailyReportDetailSideEffect()
}

@OptIn(OrbitExperimental::class)
@HiltViewModel
class DailyReportDetailViewModel @Inject constructor(
    private val getDailyReportById: GetDailyReportByIdUseCase,
    private val openDailyReport: OpenDailyReportUseCase,
) : ViewModel(),
    ContainerHost<DailyReportDetailState, DailyReportDetailSideEffect> {
    override val container: Container<DailyReportDetailState, DailyReportDetailSideEffect> =
        container(DailyReportDetailState())

    fun onAction(action: DailyReportDetailAction) {
        when (action) {
            is DailyReportDetailAction.Init -> {
                handleInit(action.id)
            }
        }
    }

    private fun handleInit(id: Long) =
        intent {
            getDailyReportById(id).handle(
                onSuccess = {
                    reduce {
                        state.copy(isLoading = false, dailyReport = it)
                    }
                    if (!it.isOpen) {
                        openNewDailyReport(id)
                    }
                },
                onError = { throwable, _ ->
                    Logger.e("getDailyReportById onError: $throwable")
                    reduce {
                        state.copy(
                            isLoading = false,
                            dailyReport = null,
                        )
                    }
                    postSideEffect(DailyReportDetailSideEffect.ShowDailyReportError)
                },
            )
        }

    private suspend fun openNewDailyReport(id: Long) =
        subIntent {
            try {
                Logger.d("openDailyReport id: $id")
                openDailyReport(id)
            } catch (e: Exception) {
                Logger.e("openDailyReport onError: $e")
                postSideEffect(DailyReportDetailSideEffect.ShowDailyReportError)
            }
        }
}
