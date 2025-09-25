package com.emotionstorage.time_capsule.presentation

import androidx.lifecycle.ViewModel
import com.emotionstorage.time_capsule.ui.model.TimeCapsuleItemState
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.viewmodel.container
import org.orbitmvi.orbit.ContainerHost
import java.time.LocalDate
import javax.inject.Inject

data class CalendarState(
    val keyCount: Int = 5,
    val calendarYearMonth: LocalDate = LocalDate.now().withDayOfMonth(1),
    val timeCapsuleDates: List<LocalDate> = emptyList(),
)

sealed class CalendarAction {
    object InitKeyCount : CalendarAction()
    data class SelectCalendarYearMonth(val yearMonth: LocalDate) : CalendarAction()
    data class SelectCalendarDate(val date: LocalDate) : CalendarAction()
}

sealed class CalendarSideEffect {
    data class ShowBottomSheet(
        val date: LocalDate,
        val timeCapsules: List<TimeCapsuleItemState>,
        val dailyReportId: String?,
        val isNewDailyReport: Boolean,
    ) : CalendarSideEffect()
}

@HiltViewModel
class CalendarViewModel @Inject constructor(
//    private val getKeyCount: GetKeyCountUseCase, 열쇠 조회
//    private val getTimeCapsuleDates: GetTimeCapsuleDatesUseCase, 타임 캡슐 존재 날짜 목록 조회
//    private val getTimeCapsulesOfDate: GetTimeCapsulesOfDateUseCase, 특정 날짜 타임 캡슐 목록 조회
//    private val getDailyReportOfDate: GetDailyReportOfDateUseCase, 특정 날짜 일일 리포트 ID 조회
) : ViewModel(), ContainerHost<CalendarState, CalendarSideEffect> {
    override val container: Container<CalendarState, CalendarSideEffect> = container(CalendarState())

    fun onAction(action: CalendarAction) {
        when (action) {
            is CalendarAction.InitKeyCount -> {
                handleInitKeyCount()
            }

            is CalendarAction.SelectCalendarYearMonth -> {
                handleSelectCalendarYearMonth(action.yearMonth)
            }

            is CalendarAction.SelectCalendarDate -> {
                handleSelectCalendarDate(action.date)
            }
        }
    }

    private fun handleInitKeyCount() = intent {
        // todo: get key count
        reduce { state.copy(keyCount = 5) }
    }

    private fun handleSelectCalendarYearMonth(yearMonth: LocalDate) =
        intent {
            // todo: get time capsule dates of selected year, month
            reduce {
                state.copy(
                    calendarYearMonth = yearMonth.withDayOfMonth(1),
                    timeCapsuleDates = emptyList()
                )
            }
        }


    private fun handleSelectCalendarDate(date: LocalDate) =
        intent {
            // todo: get time capsules of date
            // todo: get daily report id of date
            postSideEffect(
                CalendarSideEffect.ShowBottomSheet(
                    date = date,
                    timeCapsules = emptyList(),
                    dailyReportId = null,
                    isNewDailyReport = false
                )
            )
        }
}
