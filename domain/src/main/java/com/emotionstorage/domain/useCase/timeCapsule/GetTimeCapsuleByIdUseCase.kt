package com.emotionstorage.domain.useCase.timeCapsule

import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.model.TimeCapsule
import com.emotionstorage.domain.repo.TimeCapsuleRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDateTime
import javax.inject.Inject

class GetTimeCapsuleByIdUseCase @Inject constructor(
    private val timeCapsuleRepository: TimeCapsuleRepository
) {
    operator suspend fun invoke(id: String): Flow<DataState<TimeCapsule>> = timeCapsuleRepository.getTimeCapsuleById(id)
}
