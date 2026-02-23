package com.emotionstorage.data.dataSource.remote

interface CalendarRemoteDataSource {
    suspend fun getTimeCapsuleOrDailyReportExistDate(
        year: Int,
        month: Int,
    ): List<String>
}
