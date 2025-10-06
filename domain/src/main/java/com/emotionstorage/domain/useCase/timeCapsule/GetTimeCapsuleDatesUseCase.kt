package com.emotionstorage.domain.useCase.timeCapsule

import com.emotionstorage.domain.repo.TimeCapsuleRepository
import java.time.YearMonth
import javax.inject.Inject

class GetTimeCapsuleDatesUseCase @Inject constructor(
    private val timeCapsuleRepository: TimeCapsuleRepository,
) {
    suspend operator fun invoke(yearMonth: YearMonth) = timeCapsuleRepository.getTimeCapsuleDates(yearMonth)
}
