package com.emotionstorage.time_capsule.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emotionstorage.ai_chat.domain.usecase.GetChatRoomIdUseCase
import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.useCase.key.GetKeyCountUseCase
import com.emotionstorage.domain.useCase.timeCapsule.GetTimeCapsuleDatesUseCase
import com.emotionstorage.domain.useCase.timeCapsule.GetTimeCapsulesOfDateUseCase
import com.emotionstorage.time_capsule.ui.model.TimeCapsuleItemState
import com.emotionstorage.time_capsule.ui.modelMapper.TimeCapsuleMapper
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.viewmodel.container
import org.orbitmvi.orbit.ContainerHost
import java.time.LocalDate
import javax.inject.Inject

data class CalendarState(
    val keyCount: Int = 0,
    // calendar states
    val calendarYearMonth: LocalDate = LocalDate.now().withDayOfMonth(1),
    val timeCapsuleDates: List<LocalDate> = emptyList(),
    // if not null, show bottom sheet
    val calendarDate: LocalDate? = null,
    // bottom sheet states
    val timeCapsules: List<TimeCapsuleItemState> = emptyList(),
    val dailyReportId: String? = null,
    val isNewDailyReport: Boolean = false,
    // 오늘 감정 기록 여부
    val madeTimeCapsuleToday: Boolean = false,
)

sealed class CalendarAction {
    // init screen state
    object Initiate : CalendarAction()

    // set calendar year month & get time capsule dates
    data class SelectCalendarYearMonth(
        val yearMonth: LocalDate,
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
//    private val getDailyReportOfDate: GetDailyReportOfDateUseCase,
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
            // init key count
            viewModelScope.launch {
                getKeyCount().collect { result ->
                    when (result) {
                        is DataState.Success -> {
                            reduce {
                                state.copy(keyCount = result.data)
                            }
                        }

                        is DataState.Error -> {
                            Logger.e("handleInitKey error: $result")
                            reduce {
                                state.copy(keyCount = 0)
                            }
                        }

                        is DataState.Loading -> {
                            // do nothing
                        }
                    }
                }
            }

            // init calendar dates
            viewModelScope.launch {
                getTimeCapsuleDates(
                    state.calendarYearMonth.year,
                    state.calendarYearMonth.monthValue
                ).collect { result ->
                    when (result) {
                        is DataState.Success -> {
                            reduce {
                                state.copy(
                                    timeCapsuleDates = result.data,
                                )
                            }
                        }

                        is DataState.Error -> {
                            Logger.e("getTimeCapsuleDates error, $result")
                            reduce {
                                state.copy(
                                    timeCapsuleDates = emptyList(),
                                )
                            }
                        }

                        is DataState.Loading -> {
                            // do nothing
                        }
                    }
                }
            }

            // todo: init madeTimeCapsuleToday

            // show bottom sheet if calendarDate is not null
            if (state.calendarDate != null) {
                postSideEffect(CalendarSideEffect.ShowBottomSheet)
            }
        }

    private fun handleSelectCalendarYearMonth(yearMonth: LocalDate) =
        intent {
            getTimeCapsuleDates(yearMonth.year, yearMonth.monthValue).collect { result ->
                when (result) {
                    is DataState.Success -> {
                        reduce {
                            state.copy(
                                calendarYearMonth = yearMonth.withDayOfMonth(1),
                                timeCapsuleDates = result.data,
                            )
                        }
                    }

                    is DataState.Error -> {
                        Logger.e("getTimeCapsuleDates error, $result")
                        reduce {
                            state.copy(
                                calendarYearMonth = yearMonth.withDayOfMonth(1),
                                timeCapsuleDates = emptyList(),
                            )
                        }
                    }

                    is DataState.Loading -> {
                        // do nothing
                    }
                }
            }
        }

    private fun handleSelectCalendarDate(date: LocalDate) =
        intent {
            reduce {
                state.copy(calendarDate = date)
            }

            // get time capsules of date
            viewModelScope.launch {
                getTimeCapsulesOfDate(date).collect { result ->
                    when (result) {
                        is DataState.Success -> {
                            reduce {
                                state.copy(timeCapsules = result.data.map {
                                    TimeCapsuleMapper.toUi(
                                        it
                                    )
                                })
                            }
                        }

                        is DataState.Error -> {
                            Logger.e("CalendarViewModel: handleGetTimeCapsulesOfDate error: $result")
                            reduce {
                                state.copy(timeCapsules = emptyList())
                            }
                        }


                        is DataState.Loading -> {
                            // do nothing
                        }
                    }
                }

            }

            // todo: get daily report id of date

            postSideEffect(CalendarSideEffect.ShowBottomSheet)
        }

    private fun handleClearBottomSheet() = intent {
        // clear bottom sheet states
        reduce {
            state.copy(
                calendarDate = null,
                timeCapsules = emptyList(),
                dailyReportId = null,
                isNewDailyReport = false
            )
        }
    }


    private fun handleEnterChat() =
        intent {
            getChatRoomId().collect { result ->
                when (result) {
                    is DataState.Success -> {
                        postSideEffect(CalendarSideEffect.EnterCharRoomSuccess(result.data))
                    }

                    is DataState.Error -> {
                        Logger.e("CalendarViewModel: handleEnterChat error: $result")
                    }

                    is DataState.Loading -> {
                        // do nothing
                    }
                }
            }
        }
}
