package com.emotionstorage.domain.useCase.key

import com.emotionstorage.domain.common.DataState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetKeyCountUseCase @Inject constructor() {
    suspend operator fun invoke(): Flow<DataState<Int>> = flow {
        // stub logic for test
        emit(DataState.Loading(isLoading = true))
        delay(1000)
        emit(DataState.Success(data = 5))
        delay(1000)
        emit(DataState.Loading(isLoading = false))
    }

}
