package com.emotionstorage.domain.useCase.timeCapsule

import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.model.TimeCapsule
import com.emotionstorage.domain.repo.FavoriteSortBy
import com.emotionstorage.domain.repo.TimeCapsuleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDateTime
import javax.inject.Inject

class GetFavoriteTimeCapsulesUseCase @Inject constructor(
    private val timeCapsuleRepository: TimeCapsuleRepository,
) {
    suspend operator fun invoke(sortBy: FavoriteSortBy) =
        timeCapsuleRepository.getFavoriteTimeCapsules(sortBy)

//    suspend operator fun invoke(sortBy: FavoriteSortBy): Flow<DataState<List<TimeCapsule>>> =
//        flow {
//            // stub usecase
//            val timeCapsules =
//                (1..15)
//                    .toList()
//                    .map { it ->
//                        TimeCapsule(
//                            id = it.toString(),
//                            status = TimeCapsule.STATUS.OPENED,
//                            title = "오늘 아침에 친구를 만났는데, 친구가 늦었어..",
//                            summary = "",
//                            emotions =
//                                listOf(
//                                    TimeCapsule.Emotion(
//                                        emoji = "\uD83D\uDE14",
//                                        label = "서운함",
//                                        percentage = 30.0f,
//                                    ),
//                                    TimeCapsule.Emotion(
//                                        emoji = "\uD83D\uDE0A",
//                                        label = "고마움",
//                                        percentage = 30.0f,
//                                    ),
//                                    TimeCapsule.Emotion(
//                                        emoji = "\uD83E\uDD70",
//                                        label = "안정감",
//                                        percentage = 80.0f,
//                                    ),
//                                ),
//                            isFavorite = true,
//                            favoriteAt = LocalDateTime.now(),
//                            createdAt = LocalDateTime.now(),
//                        )
//                    }.sortedByDescending {
//                        when (sortBy) {
//                            FavoriteSortBy.FAVORITE_AT -> it.favoriteAt
//                            FavoriteSortBy.NEWEST -> it.createdAt
//                        }
//                    }
//            emit(DataState.Success(timeCapsules))
//        }
}
