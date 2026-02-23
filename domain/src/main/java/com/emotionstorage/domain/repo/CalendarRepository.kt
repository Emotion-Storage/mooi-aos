package com.emotionstorage.domain.repo

import java.time.LocalDate

interface CalendarRepository {
    suspend fun getTimeCapsuleOrDailyReportExistDate(
        year: Int,
        month: Int,
    ): Set<LocalDate>
}
