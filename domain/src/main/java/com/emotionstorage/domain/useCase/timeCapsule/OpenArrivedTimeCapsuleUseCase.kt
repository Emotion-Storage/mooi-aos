package com.emotionstorage.domain.useCase.timeCapsule

import com.emotionstorage.domain.repo.TimeCapsuleRepository
import javax.inject.Inject

class OpenArrivedTimeCapsuleUseCase @Inject constructor(
    private val timeCapsuleRepository: TimeCapsuleRepository,
) {
    suspend operator fun invoke(id: String) = timeCapsuleRepository.openArrivedTimeCapsule(id)
}
