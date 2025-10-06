package com.emotionstorage.domain.useCase.timeCapsule

import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.repo.TimeCapsuleRepository
import com.emotionstorage.domain.repo.SetFavoriteResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SetFavoriteTimeCapsuleUseCase @Inject constructor(
    private val timeCapsuleRepository: TimeCapsuleRepository,
) {
    operator fun invoke(
        id: String,
        isFavorite: Boolean,
    ): Flow<DataState<SetFavoriteResult>> = timeCapsuleRepository.setFavoriteTimeCapsule(id, isFavorite)
}
