package com.emotionstorage.domain.useCase.timeCapsule

import androidx.paging.PagingData
import com.emotionstorage.domain.model.TimeCapsule
import com.emotionstorage.domain.repo.TimeCapsuleRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class GetPagedArrivedTimeCapsulesUseCase @Inject constructor(
    private val timeCapsuleRepository: TimeCapsuleRepository,
) {
    suspend operator fun invoke(): Flow<PagingData<TimeCapsule>> =
        timeCapsuleRepository.getPagedTimeCapsules(
            startDate = LocalDate.now(),
            endDate = LocalDate.now().plusWeeks(3),
            status = "arrived",
        )
}
