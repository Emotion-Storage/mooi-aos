package com.emotionstorage.remote.dataSourceImpl

import com.emotionstorage.data.dataSource.remote.CalendarRemoteDataSource
import com.emotionstorage.remote.api.CalendarApiService
import javax.inject.Inject

class CalendarRemoteDataSourceImpl @Inject constructor(
    private val calendarApiService: CalendarApiService,
) : CalendarRemoteDataSource {
    override suspend fun getTimeCapsuleOrDailyReportExistDate(
        year: Int,
        month: Int,
    ): List<String> {
        val response = calendarApiService.getTimeCapsuleOrDailyReportExistDate(year, month)
        return response.data?.dates ?: emptyList()
    }
}
