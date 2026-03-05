package com.emotionstorage.domain.useCase.calendar

import com.emotionstorage.domain.repo.CalendarRepository
import java.time.LocalDate
import javax.inject.Inject

class GetCalendarContentDatesUseCase @Inject constructor(
    private val calendarRepository: CalendarRepository,
) {
    suspend operator fun invoke(
        year: Int,
        month: Int,
    ): Set<LocalDate> = calendarRepository.getTimeCapsuleOrDailyReportExistDate(year, month)
}
