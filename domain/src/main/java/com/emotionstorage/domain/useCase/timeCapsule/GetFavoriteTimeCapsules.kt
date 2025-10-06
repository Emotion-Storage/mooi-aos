package com.emotionstorage.domain.useCase.timeCapsule

import com.emotionstorage.domain.repo.FavoriteSortBy
import com.emotionstorage.domain.repo.TimeCapsuleRepository
import javax.inject.Inject

class GetFavoriteTimeCapsules @Inject constructor(
    private val timeCapsuleRepository: TimeCapsuleRepository,
) {
    suspend operator fun invoke(sortBy: FavoriteSortBy) = timeCapsuleRepository.getFavoriteTimeCapsules(sortBy)
}
