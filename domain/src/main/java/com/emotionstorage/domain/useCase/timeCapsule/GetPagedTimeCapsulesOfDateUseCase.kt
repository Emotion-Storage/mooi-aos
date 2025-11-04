package com.emotionstorage.domain.useCase.timeCapsule

import androidx.paging.PagingData
import com.emotionstorage.domain.model.TimeCapsule
import com.emotionstorage.domain.repo.TimeCapsuleRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class GetPagedTimeCapsulesOfDateUseCase @Inject constructor(
    private val timeCapsuleRepository: TimeCapsuleRepository,
) {
    operator fun invoke(date: LocalDate): Flow<PagingData<TimeCapsule>> =
        timeCapsuleRepository.getPagedTimeCapsules(
            startDate = date,
            endDate = date,
            status = "arrived",
        )
}
