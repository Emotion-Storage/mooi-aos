package com.emotionstorage.domain.useCase.dailyReport

import com.emotionstorage.domain.common.DataState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDate

class GetDailyReportOfDateUseCase {
    suspend operator fun invoke(date: LocalDate): Flow<DataState<GetDailyReportOfDateResponse>> = flow{
        // stub logic for test
        emit(DataState.Loading(isLoading = true))
        delay(1500)
        emit(DataState.Success(GetDailyReportOfDateResponse(
            dailyReportId = "dummy-id",
            isNewDailyReport = true
        )))
        delay(1500)
        emit(DataState.Loading(isLoading = false))
    }

    data class GetDailyReportOfDateResponse(
        val dailyReportId: String? = null,
        val isNewDailyReport: Boolean = false
    )
}
