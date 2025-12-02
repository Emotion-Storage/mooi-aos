package com.emotionstorage.domain.useCase.timeCapsule

import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.repo.TimeCapsuleRepository
import javax.inject.Inject

class SetFavoriteTimeCapsuleUseCase @Inject constructor(
    private val timeCapsuleRepository: TimeCapsuleRepository,
) {
    suspend operator fun invoke(
        id: Long,
        isFavorite: Boolean,
    ): DataState<Boolean> = timeCapsuleRepository.setFavoriteTimeCapsule(id, isFavorite)
}
