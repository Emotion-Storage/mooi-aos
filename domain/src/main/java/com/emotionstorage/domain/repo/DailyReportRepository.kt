package com.emotionstorage.domain.repo

import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.model.DailyReport
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface DailyReportRepository {
    suspend fun getDailyReport(date: LocalDate): Flow<DataState<DailyReport>>
}
