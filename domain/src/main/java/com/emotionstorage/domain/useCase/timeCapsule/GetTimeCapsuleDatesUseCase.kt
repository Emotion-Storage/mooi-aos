package com.emotionstorage.domain.useCase.timeCapsule

import com.emotionstorage.domain.common.DataState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import javax.inject.Inject

class GetTimeCapsuleDatesUseCase @Inject constructor() {
    suspend operator fun invoke(
        year: Int,
        month: Int,
    ): Flow<DataState<List<LocalDate>>> =
        flow {
            // stub logic for test
            emit(DataState.Loading(true))
            delay(1500)
            emit(DataState.Success((1..30).filter { it % 2 == 0 }.map { LocalDate.of(year, month, it) }))
            delay(1500)
            emit(DataState.Loading(false))
        }
}
