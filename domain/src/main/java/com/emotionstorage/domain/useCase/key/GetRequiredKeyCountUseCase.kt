package com.emotionstorage.domain.useCase.key

import com.emotionstorage.domain.common.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import javax.inject.Inject

class GetRequiredKeyCountUseCase @Inject constructor() {
    operator fun invoke(arriveAt: LocalDate): Flow<DataState<Int>> =
        flow {
            when (LocalDate.now().getDaysBetween(arriveAt).absoluteValue) {
                in 0..7 -> emit(DataState.Success(1))
                in 8..30 -> emit(DataState.Success(3))
                in 31..90 -> emit(DataState.Success(7))
                in 91..180 -> emit(DataState.Success(11))
                in 181..365 -> emit(DataState.Success(15))
                else -> emit(DataState.Error(throwable = Throwable("invalid arriveAt")))
            }
        }
}
