package com.emotionstorage.domain.useCase.dailyReport

import com.emotionstorage.domain.repo.DailyReportRepository
import javax.inject.Inject

class OpenDailyReportUseCase @Inject constructor(
    private val dailyReportRepository: DailyReportRepository,
) {
    suspend operator fun invoke(id: Long): Boolean = dailyReportRepository.openDailyReport(id)
}
