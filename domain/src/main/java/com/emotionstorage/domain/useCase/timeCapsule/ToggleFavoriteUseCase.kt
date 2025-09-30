package com.emotionstorage.domain.useCase.timeCapsule

import com.emotionstorage.domain.common.DataState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor() {
    operator fun invoke(id: String): Flow<DataState<ToggleToastResult>> = flow {
        // stub logic for test
        emit(DataState.Loading(isLoading = true))
        delay(1000)
        emit(DataState.Success(ToggleToastResult.FAVORITE_ADDED))
        delay(1000)
        emit(DataState.Success(ToggleToastResult.FAVORITE_REMOVED))
        delay(1000)
        emit(
            DataState.Error(
                throwable = Throwable("toggle favorite error"),
                data = ToggleToastResult.FAVORITE_FULL
            )
        )
        delay(1000)
        emit(DataState.Loading(isLoading = false))
    }

    enum class ToggleToastResult {
        FAVORITE_ADDED,
        FAVORITE_REMOVED,
        FAVORITE_FULL,
    }
}
