package com.emotionstorage.time_capsule.presentation

import androidx.lifecycle.ViewModel
import com.emotionstorage.ai_chat.domain.usecase.GetChatRoomIdUseCase
import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.model.TimeCapsule.Emotion
import com.emotionstorage.domain.model.TimeCapsule.STATUS
import com.emotionstorage.domain.useCase.timeCapsule.GetTimeCapsuleDatesUseCase
import com.emotionstorage.time_capsule.ui.model.TimeCapsuleItemState
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.viewmodel.container
import org.orbitmvi.orbit.ContainerHost
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

data class CalendarState(
    val keyCount: Int = 5,
    val calendarYearMonth: LocalDate = LocalDate.now().withDayOfMonth(1),
    val timeCapsuleDates: List<LocalDate> = emptyList(),
    // if not null, show bottom sheet
    val calendarDate: LocalDate? = null,
    // 오늘 감정 기록 여부
    val madeTimeCapsuleToday: Boolean = false,
)

sealed class CalendarAction {
    object Initiate : CalendarAction()

    data class SelectCalendarYearMonth(
        val yearMonth: LocalDate,
    ) : CalendarAction()

    data class SelectCalendarDate(
        val date: LocalDate,
    ) : CalendarAction()

    object ClearDalendarDate : CalendarAction()

    object EnterChat : CalendarAction()
}

sealed class CalendarSideEffect {
    data class ShowBottomSheet(
        val date: LocalDate,
        val timeCapsules: List<TimeCapsuleItemState>,
        val dailyReportId: String?,
        val isNewDailyReport: Boolean,
    ) : CalendarSideEffect()

    data class EnterCharRoomSuccess(
        val roomId: String,
    ) : CalendarSideEffect()
}

@HiltViewModel
class CalendarViewModel @Inject constructor(
//    private val getKeyCount: GetKeyCountUseCase,
    private val getTimeCapsuleDates: GetTimeCapsuleDatesUseCase,
//    private val getTimeCapsulesOfDate: GetTimeCapsulesOfDateUseCase,
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

            is CalendarAction.ClearDalendarDate -> {
                intent {
                    reduce { state.copy(calendarDate = null) }
                }
            }

            is CalendarAction.EnterChat -> {
                handleEnterChat()
            }
        }
    }

    private fun handleInitiate() =
        intent {
            // todo: init key count
            reduce { state.copy(keyCount = 5) }

            // init calendar
            handleSelectCalendarYearMonth(state.calendarYearMonth)

            // todo: init madeTimeCapsuleToday

            // show bottom sheet if calendarDate is not null
            if (state.calendarDate != null) {
                handleSelectCalendarDate(state.calendarDate!!)
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

            // todo: get time capsules of date
            // todo: get daily report id of date
            postSideEffect(
                CalendarSideEffect.ShowBottomSheet(
                    date = date,
                    timeCapsules =
                        (0..3).map { i ->
                            TimeCapsuleItemState(
                                id = i.toString(),
                                status = STATUS.entries.get(i),
                                title = "오늘 아침에 친구를 만났는데, 친구가 늦었어..",
                                emotions =
                                    listOf(
                                        Emotion(
                                            label = "서운함",
                                            icon = 0,
                                        ),
                                        Emotion(
                                            label = "화남",
                                            icon = 1,
                                        ),
                                        Emotion(
                                            label = "피곤함",
                                            icon = 2,
                                        ),
                                    ),
                                isFavorite = false,
                                isFavoriteAt = null,
                                createdAt = LocalDateTime.now(),
                                openDday = -99,
                            )
                        },
                    dailyReportId = null,
                    isNewDailyReport = false,
                ),
            )
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
