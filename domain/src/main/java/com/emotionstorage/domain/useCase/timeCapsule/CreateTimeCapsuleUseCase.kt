package com.emotionstorage.domain.useCase.timeCapsule

import com.emotionstorage.domain.repo.TimeCapsuleRepository
import javax.inject.Inject

class CreateTimeCapsuleUseCase @Inject constructor(
    private val repository: TimeCapsuleRepository,
) {
    suspend operator fun invoke(id: Long) = repository.createTimeCapsule(id)
}
