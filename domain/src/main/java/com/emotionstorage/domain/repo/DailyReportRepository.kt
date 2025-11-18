package com.emotionstorage.domain.repo

import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.model.DailyReport
import java.time.LocalDate

interface DailyReportRepository {
    suspend fun getDailyReport(date: LocalDate): DataState<DailyReport>

    suspend fun getDailyReport(id: Long): DataState<DailyReport>

    suspend fun openDailyReport(id: Long): Boolean
}
