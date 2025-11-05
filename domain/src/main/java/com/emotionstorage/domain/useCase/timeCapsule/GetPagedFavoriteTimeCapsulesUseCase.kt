package com.emotionstorage.domain.useCase.timeCapsule

import androidx.paging.PagingData
import com.emotionstorage.domain.model.TimeCapsule
import com.emotionstorage.domain.repo.FavoriteSortBy
import com.emotionstorage.domain.repo.TimeCapsuleRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPagedFavoriteTimeCapsulesUseCase @Inject constructor(
    private val timeCapsuleRepository: TimeCapsuleRepository,
) {
    operator fun invoke(sortBy: FavoriteSortBy): Flow<PagingData<TimeCapsule>> =
        timeCapsuleRepository.getPagedFavoriteTimeCapsules(sortBy)
}
