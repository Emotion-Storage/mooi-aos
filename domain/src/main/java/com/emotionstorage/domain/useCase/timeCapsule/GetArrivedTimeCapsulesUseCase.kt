package com.emotionstorage.domain.useCase.timeCapsule

import com.emotionstorage.domain.repo.TimeCapsuleRepository
import java.time.LocalDate
import javax.inject.Inject

class GetArrivedTimeCapsulesUseCase @Inject constructor(
    private val timeCapsuleRepository: TimeCapsuleRepository
) {
    suspend operator fun invoke(page: Int = 1) = timeCapsuleRepository.getTimeCapsules(
        startDate = LocalDate.now(),
        endDate = LocalDate.now().plusWeeks(3),
        page = page,
        status = "arrived"
    )
}
