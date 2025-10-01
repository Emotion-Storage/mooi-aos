package com.emotionstorage.time_capsule_detail.domain.useCase

import com.emotionstorage.common.getDaysBetween
import com.emotionstorage.domain.common.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import javax.inject.Inject
import kotlin.math.absoluteValue

class GetRequiredKeyCountUseCase @Inject constructor() {
    operator fun invoke(arriveAt: LocalDate): Flow<DataState<Int>> =
        flow {
            when (LocalDate.now().getDaysBetween(arriveAt).absoluteValue) {
                in 0..7 -> emit(DataState.Success(1))
                in 8..30 -> emit(DataState.Success(3))
                in 31..90 -> emit(DataState.Success(7))
                in 90..180 -> emit(DataState.Success(11))
                in 180..365 -> emit(DataState.Success(15))
                else -> emit(DataState.Error(throwable = Throwable("invalid arriveAt")))
            }
        }
}
