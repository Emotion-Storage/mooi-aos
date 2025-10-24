package com.emotionstorage.domain.useCase.timeCapsule

import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.model.TimeCapsule
import com.emotionstorage.domain.repo.TimeCapsuleRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class GetTimeCapsulesOfDateUseCase @Inject constructor(
    private val timeCapsuleRepository: TimeCapsuleRepository,
) {
    suspend operator fun invoke(date: LocalDate): Flow<DataState<List<TimeCapsule>>> =
        timeCapsuleRepository.getTimeCapsules(
            startDate = date,
            endDate = date,
            page = 1,
            status = "arrived",
        )
}
