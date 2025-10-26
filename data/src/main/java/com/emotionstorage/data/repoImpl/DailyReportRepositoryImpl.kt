package com.emotionstorage.data.repoImpl

import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.model.DailyReport
import com.emotionstorage.domain.repo.DailyReportRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class DailyReportRepositoryImpl @Inject constructor(): DailyReportRepository {
    override suspend fun getDailyReport(date: LocalDate): Flow<DataState<DailyReport>> {
        TODO("Not yet implemented")
    }
}
