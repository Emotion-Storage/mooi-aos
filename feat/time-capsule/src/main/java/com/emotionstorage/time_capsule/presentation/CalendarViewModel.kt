package com.emotionstorage.time_capsule.presentation

import androidx.lifecycle.ViewModel
import com.emotionstorage.ai_chat.domain.usecase.GetChatRoomIdUseCase
import com.emotionstorage.domain.common.collectDataState
import com.emotionstorage.domain.useCase.dailyReport.GetDailyReportOfDateUseCase
import com.emotionstorage.domain.useCase.key.GetKeyCountUseCase
import com.emotionstorage.domain.useCase.timeCapsule.GetTimeCapsuleDatesUseCase
import com.emotionstorage.domain.useCase.timeCapsule.GetTimeCapsulesOfDateUseCase
import com.emotionstorage.time_capsule.ui.model.TimeCapsuleItemState
import com.emotionstorage.time_capsule.ui.modelMapper.TimeCapsuleMapper
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.viewmodel.container
import org.orbitmvi.orbit.ContainerHost
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

data class CalendarState(
    val keyCount: Int = 0,
    val madeTimeCapsuleToday: Boolean = false,
    // calendar states
    val calendarYearMonth: YearMonth = YearMonth.now(),
    val timeCapsuleDates: List<LocalDate> = emptyList(),
    val calendarDate: LocalDate? = null,
    // bottom sheet states
    val timeCapsules: List<TimeCapsuleItemState> = emptyList(),
    val dailyReportId: String? = null,
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
    data class SelectCalendarDate(
        val date: LocalDate,
    ) : CalendarAction()

    // reset bottom sheet states
    object ClearBottomSheet : CalendarAction()

    // get chat room id
    object EnterChat : CalendarAction()
}

sealed class CalendarSideEffect {
    object ShowBottomSheet : CalendarSideEffect()

    data class EnterCharRoomSuccess(
        val roomId: String,
    ) : CalendarSideEffect()
}

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val getKeyCount: GetKeyCountUseCase,
    private val getTimeCapsuleDates: GetTimeCapsuleDatesUseCase,
    private val getTimeCapsulesOfDate: GetTimeCapsulesOfDateUseCase,
    private val getDailyReportOfDate: GetDailyReportOfDateUseCase,
    private val getChatRoomId: GetChatRoomIdUseCase,
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

            is CalendarAction.SelectCalendarDate -> {
                handleSelectCalendarDate(action.date)
            }

            is CalendarAction.ClearBottomSheet -> {
                handleClearBottomSheet()
            }

            is CalendarAction.EnterChat -> {
                handleEnterChat()
            }
        }
    }

    private fun handleInitiate() =
        intent {
            coroutineScope {
                // init key count
                launch {
                    collectDataState(
                        flow = getKeyCount(),
                        onSuccess = { data ->
                            reduce {
                                state.copy(keyCount = data)
                            }
                        },
                        onError = { throwable, _ ->
                            Logger.e("handleInitKey error: $throwable")
                            reduce {
                                state.copy(keyCount = 0)
                            }
                        },
                    )
                }

                // init calendar dates
                launch {
                    collectDataState(
                        flow =
                            getTimeCapsuleDates(
                                state.calendarYearMonth.year,
                                state.calendarYearMonth.monthValue,
                            ),
                        onSuccess = { data ->
                            reduce {
                                state.copy(
                                    timeCapsuleDates = data,
                                )
                            }
                        },
                        onError = { throwable, _ ->
                            Logger.e("getTimeCapsuleDates error, $throwable")
                            reduce {
                                state.copy(
                                    timeCapsuleDates = emptyList(),
                                )
                            }
                        },
                    )
                }

                // init madeTimeCapsuleToday
                launch {
                    collectDataState(
                        flow = getTimeCapsulesOfDate(LocalDate.now()),
                        onSuccess = { data ->
                            reduce {
                                state.copy(madeTimeCapsuleToday = data.isNotEmpty())
                            }
                        },
                        onError = { throwable, _ ->
                            Logger.e("CalendarViewModel: handleGetTimeCapsulesOfDate error: $throwable")
                            reduce {
                                state.copy(madeTimeCapsuleToday = false)
                            }
                        },
                    )
                }
            }

            // show bottom sheet if calendarDate is not null
            if (state.calendarDate != null) {
                postSideEffect(CalendarSideEffect.ShowBottomSheet)
            }
        }

    private fun handleSelectCalendarYearMonth(yearMonth: YearMonth) =
        intent {
            collectDataState(
                flow = getTimeCapsuleDates(yearMonth.year, yearMonth.monthValue),
                onSuccess = { data ->
                    reduce {
                        state.copy(
                            calendarYearMonth = yearMonth,
                            timeCapsuleDates = data,
                        )
                    }
                },
                onError = { throwable, _ ->
                    Logger.e("getTimeCapsuleDates error, $throwable")
                    reduce {
                        state.copy(
                            calendarYearMonth = yearMonth,
                            timeCapsuleDates = emptyList(),
                        )
                    }
                },
            )
        }

    private fun handleSelectCalendarDate(date: LocalDate) =
        intent {
            reduce {
                state.copy(calendarDate = date)
            }

            coroutineScope {
                // get time capsules of date
                launch {
                    collectDataState(
                        flow = getTimeCapsulesOfDate(date),
                        onSuccess = { data ->
                            reduce {
                                state.copy(timeCapsules = data.map { TimeCapsuleMapper.toUi(it) })
                            }
                        },
                        onError = { throwable, _ ->
                            Logger.e("CalendarViewModel: handleGetTimeCapsulesOfDate error: $throwable")
                            reduce {
                                state.copy(timeCapsules = emptyList())
                            }
                        },
                    )
                }

                // get daily report of day
                launch {
                    collectDataState(
                        flow = getDailyReportOfDate(date),
                        onSuccess = { data ->
                            reduce {
                                state.copy(
                                    dailyReportId = data.dailyReportId,
                                    isNewDailyReport = data.isNewDailyReport,
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
            }

            postSideEffect(CalendarSideEffect.ShowBottomSheet)
        }

    private fun handleClearBottomSheet() =
        intent {
            // clear bottom sheet states
            reduce {
                state.copy(
                    calendarDate = null,
                    timeCapsules = emptyList(),
                    dailyReportId = null,
                    isNewDailyReport = false,
                )
            }
        }

    private fun handleEnterChat() =
        intent {
            collectDataState(
                flow = getChatRoomId(),
                onSuccess = { data ->
                    postSideEffect(CalendarSideEffect.EnterCharRoomSuccess(data))
                },
                onError = { throwable, _ ->
                    Logger.e("CalendarViewModel: handleEnterChat error: $throwable")
                },
            )
        }
}
