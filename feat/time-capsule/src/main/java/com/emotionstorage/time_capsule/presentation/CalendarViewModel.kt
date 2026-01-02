package com.emotionstorage.time_capsule.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.emotionstorage.domain.common.collectDataState
import com.emotionstorage.domain.useCase.dailyReport.GetDailyReportOfDateUseCase
import com.emotionstorage.domain.useCase.key.GetKeyCountUseCase
import com.emotionstorage.domain.useCase.timeCapsule.GetPagedTimeCapsulesOfDateUseCase
import com.emotionstorage.domain.useCase.timeCapsule.GetTimeCapsuleDatesUseCase
import com.emotionstorage.time_capsule.ui.model.TimeCapsuleItemState
import com.emotionstorage.time_capsule.ui.modelMapper.TimeCapsuleMapper
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.viewmodel.container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

data class CalendarState(
    val keyCount: Int? = null,
    // calendar states
    val calendarYearMonth: YearMonth = YearMonth.now(),
    val calendarTimeCapsuleDates: List<LocalDate> = emptyList(),
    val calendarSelectedDate: LocalDate? = null,
    // bottom sheet states
    val timeCapsulesFlow: Flow<PagingData<TimeCapsuleItemState>>? = null,
    val dailyReportId: Long? = null,
    val isNewDailyReport: Boolean = false,
)

sealed class CalendarAction {
    // init screen state
    object Initiate : CalendarAction()

    // set calendar year month & get time capsule dates
    data class SelectCalendarYearMonth(
        val yearMonth: YearMonth,
    ) : CalendarAction()

    // set calendar date & get bottom sheet states
    data class OpenCalendarBottomSheet(
        val date: LocalDate,
    ) : CalendarAction()

    // reset bottom sheet states
    object ClearBottomSheet : CalendarAction()
}

sealed class CalendarSideEffect {
    object ShowTimeCapsuleBottomSheet : CalendarSideEffect()
}

@OptIn(OrbitExperimental::class)
@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val getKeyCount: GetKeyCountUseCase,
    private val getTimeCapsuleDates: GetTimeCapsuleDatesUseCase,
    private val getTimeCapsulesOfDate: GetPagedTimeCapsulesOfDateUseCase,
    private val getDailyReportOfDate: GetDailyReportOfDateUseCase,
) : ViewModel(),
    ContainerHost<CalendarState, CalendarSideEffect> {
    override val container: Container<CalendarState, CalendarSideEffect> =
        container(CalendarState())

    fun onAction(action: CalendarAction) {
        when (action) {
            is CalendarAction.Initiate -> {
                handleInitiate()
            }

            is CalendarAction.SelectCalendarYearMonth -> {
                handleSelectCalendarYearMonth(action.yearMonth)
            }

            is CalendarAction.OpenCalendarBottomSheet -> {
                handleSelectCalendarDate(action.date)
            }

            is CalendarAction.ClearBottomSheet -> {
                handleClearBottomSheet()
            }
        }
    }

    private fun handleInitiate() =
        intent {
            initKeyCount()
            handleSelectCalendarYearMonth(YearMonth.from(LocalDate.now()))
        }

    private suspend fun initKeyCount() =
        subIntent {
            getKeyCount().handle(
                onSuccess = { data ->
                    reduce {
                        state.copy(keyCount = data)
                    }
                },
                onError = { throwable, _ ->
                    Logger.e("handleInitKey error: $throwable")
                    reduce {
                        state.copy(keyCount = null)
                    }
                },
            )
        }

    private fun handleSelectCalendarYearMonth(yearMonth: YearMonth) =
        intent {
            collectDataState(
                flow = getTimeCapsuleDates(yearMonth),
                onSuccess = { data ->
                    reduce {
                        state.copy(
                            calendarYearMonth = yearMonth,
                            calendarTimeCapsuleDates = data,
                        )
                    }
                },
                onError = { throwable, code, data ->
                    Logger.e("getTimeCapsuleDates error, $throwable")
                    reduce {
                        state.copy(
                            calendarYearMonth = yearMonth,
                            calendarTimeCapsuleDates = emptyList(),
                        )
                    }
                },
            )
        }

    private fun handleSelectCalendarDate(date: LocalDate) =
        intent {
            require(date in state.calendarTimeCapsuleDates)

            reduce {
                state.copy(calendarSelectedDate = date)
            }
            setTimeCapsulesFlow(date)
            setDailyReportState(date)

            postSideEffect(CalendarSideEffect.ShowTimeCapsuleBottomSheet)
        }

    private suspend fun setTimeCapsulesFlow(date: LocalDate) =
        subIntent {
            try {
                reduce {
                    state.copy(
                        timeCapsulesFlow =
                            getTimeCapsulesOfDate(date)
                                .cachedIn(viewModelScope)
                                .map {
                                    it.map { timeCapsule ->
                                        TimeCapsuleMapper.toUi(timeCapsule)
                                    }
                                },
                    )
                }
            } catch (e: Exception) {
                Logger.e("CalendarViewModel: handleGetTimeCapsulesOfDate error: $e")
                reduce {
                    state.copy(timeCapsulesFlow = null)
                }
            }
        }

    private suspend fun setDailyReportState(date: LocalDate) =
        subIntent {
            getDailyReportOfDate(date).handle(
                onSuccess = { data ->
                    reduce {
                        state.copy(
                            dailyReportId = data.id,
                            isNewDailyReport = !data.isOpen,
                        )
                    }
                },
                onError = { throwable, _ ->
                    Logger.e("CalendarViewModel: handleGetDailyReportOfDate error: $throwable")
                    reduce {
                        state.copy(
                            dailyReportId = null,
                            isNewDailyReport = false,
                        )
                    }
                },
            )
        }

    private fun handleClearBottomSheet() =
        intent {
            // clear bottom sheet states
            reduce {
                state.copy(
                    calendarSelectedDate = null,
                    timeCapsulesFlow = null,
                    dailyReportId = null,
                    isNewDailyReport = false,
                )
            }
        }
}
