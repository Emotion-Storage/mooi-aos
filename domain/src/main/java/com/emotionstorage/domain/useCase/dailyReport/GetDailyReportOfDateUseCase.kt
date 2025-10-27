package com.emotionstorage.domain.useCase.dailyReport

import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.model.DailyReport
import com.emotionstorage.domain.repo.DailyReportRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class GetDailyReportOfDateUseCase @Inject constructor(
    private val dailyReportRepository: DailyReportRepository,
) {
    suspend operator fun invoke(date: LocalDate): Flow<DataState<DailyReport>> =
        dailyReportRepository.getDailyReport(date)
}
