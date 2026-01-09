package com.emotionstorage.domain.useCase.timeCapsule

import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.useCase.home.GetHomeUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetHasNewTimeCapsuleUseCase @Inject constructor(
    private val getHomeUseCase: GetHomeUseCase
) {
    suspend operator fun invoke(): Flow<DataState<Boolean>> {
        return getHomeUseCase().map {
            if (it is DataState.Success) {
                DataState.Success(it.data.hasNewTimeCapsule)
            } else {
                it
            } as DataState<Boolean>
        }
    }
}
