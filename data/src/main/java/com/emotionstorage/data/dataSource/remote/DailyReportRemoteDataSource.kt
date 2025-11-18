package com.emotionstorage.data.dataSource.remote

import com.emotionstorage.data.model.DailyReportEntity
import java.time.LocalDate

interface DailyReportRemoteDataSource {
    suspend fun getDailyReport(date: LocalDate): DailyReportEntity

    suspend fun getDailyReport(id: Long): DailyReportEntity

    suspend fun openDailyReport(id: Long): Boolean
}
