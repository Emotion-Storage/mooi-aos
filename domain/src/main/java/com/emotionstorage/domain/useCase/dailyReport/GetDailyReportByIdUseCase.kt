package com.emotionstorage.domain.useCase.dailyReport

import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.model.DailyReport
import com.emotionstorage.domain.repo.DailyReportRepository
import javax.inject.Inject

class GetDailyReportByIdUseCase @Inject constructor(
    private val dailyReportRepository: DailyReportRepository,
) {
    suspend operator fun invoke(id: Long): DataState<DailyReport> = dailyReportRepository.getDailyReport(id)
}
