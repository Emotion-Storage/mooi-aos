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
    operator fun invoke(): Flow<PagingData<TimeCapsule>> =
        timeCapsuleRepository.getPagedTimeCapsules(
//            startDate = LocalDate.now(),
//            endDate = LocalDate.now().plusWeeks(3),
            startDate = LocalDate.of(2024,1, 1),
            endDate = LocalDate.of(2024,12, 31),
            status = "arrived",

        )
}
