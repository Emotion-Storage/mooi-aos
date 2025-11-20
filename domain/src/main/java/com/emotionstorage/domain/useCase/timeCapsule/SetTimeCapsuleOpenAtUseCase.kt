package com.emotionstorage.domain.useCase.timeCapsule

import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.repo.TimeCapsuleRepository
import java.time.LocalDateTime
import javax.inject.Inject

class SetTimeCapsuleOpenAtUseCase @Inject constructor(
    private val timeCapsuleRepository: TimeCapsuleRepository,
) {
    suspend operator fun invoke(
        id: Long,
        openAt: LocalDateTime,
    ): DataState<Unit> = timeCapsuleRepository.setTimeCapsuleOpenAt(
        id, openAt
    )
}
