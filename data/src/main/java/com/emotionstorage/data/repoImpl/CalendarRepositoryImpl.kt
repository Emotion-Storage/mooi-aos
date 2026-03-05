package com.emotionstorage.data.repoImpl

import com.emotionstorage.data.dataSource.remote.CalendarRemoteDataSource
import com.emotionstorage.domain.repo.CalendarRepository
import java.time.LocalDate
import javax.inject.Inject

class CalendarRepositoryImpl @Inject constructor(
    private val calendarRemoteDataSource: CalendarRemoteDataSource,
) : CalendarRepository {
    override suspend fun getTimeCapsuleOrDailyReportExistDate(
        year: Int,
        month: Int,
    ): Set<LocalDate> {
        val dates = calendarRemoteDataSource.getTimeCapsuleOrDailyReportExistDate(year, month)

        return dates.mapNotNull { runCatching { LocalDate.parse(it) }.getOrNull() }.toSet()
    }
}
