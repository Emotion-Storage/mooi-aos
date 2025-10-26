package com.emotionstorage.domain.useCase.timeCapsule

import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.model.TimeCapsule
import com.emotionstorage.domain.repo.TimeCapsuleRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTimeCapsuleByIdUseCase @Inject constructor(
    private val timeCapsuleRepository: TimeCapsuleRepository,
) {
    suspend operator fun invoke(id: Long): Flow<DataState<TimeCapsule>> = timeCapsuleRepository.getTimeCapsuleById(id)
}
