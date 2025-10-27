package com.emotionstorage.data.dataSource

import com.emotionstorage.data.model.DailyReportEntity
import java.time.LocalDate

interface DailyReportRemoteDataSource {
    suspend fun getDailyReport(date: LocalDate): DailyReportEntity
}
