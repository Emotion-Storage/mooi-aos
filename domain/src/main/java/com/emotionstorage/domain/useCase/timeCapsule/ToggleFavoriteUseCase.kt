package com.emotionstorage.domain.useCase.timeCapsule

import com.emotionstorage.domain.common.DataState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor() {
    operator fun invoke(id: String): Flow<DataState<ToggleFavoriteResult>> = flow {
        // stub logic for test
        emit(DataState.Loading(isLoading = true))
        delay(1000)
        emit(
            DataState.Success(
                ToggleFavoriteResult(isSuccess = true, isFavorite = true)
            )
        )
        delay(1000)
        emit(DataState.Loading(isLoading = false))
    }

    data class ToggleFavoriteResult(
        val isSuccess: Boolean,
        val isFavorite: Boolean,
        val isFavoriteFull: Boolean = false,
    )
}
