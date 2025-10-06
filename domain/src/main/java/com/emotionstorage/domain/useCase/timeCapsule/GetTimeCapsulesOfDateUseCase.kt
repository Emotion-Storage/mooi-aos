package com.emotionstorage.domain.useCase.timeCapsule

import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.model.TimeCapsule
import com.emotionstorage.domain.model.TimeCapsule.Emotion
import com.emotionstorage.domain.model.TimeCapsule.STATUS
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import javax.inject.Inject

class GetTimeCapsulesOfDateUseCase @Inject constructor() {
    operator fun invoke(date: LocalDate): Flow<DataState<List<TimeCapsule>>> =
        flow {
            // stub logic for test
            emit(DataState.Loading(isLoading = true))
            delay(1000)
            emit(
                DataState.Success(
                    data =
                        listOf(
                            TimeCapsule(
                                id = "dummy-id",
                                status = STATUS.TEMPORARY,
                                title = "오늘 아침에 친구를 만났는데, 친구가 늦었어..",
                                summary = "",
                                emotions =
                                    listOf(
                                        TimeCapsule.Emotion(
                                            emoji = "\uD83D\uDE14",
                                            label = "서운함",
                                            percentage = 30.0f
                                        ),
                                        TimeCapsule.Emotion(
                                            emoji = "\uD83D\uDE0A",
                                            label = "고마움",
                                            percentage = 30.0f
                                        ),
                                        TimeCapsule.Emotion(
                                            emoji = "\uD83E\uDD70",
                                            label = "안정감",
                                            percentage = 80.0f
                                        ),
                                    ),
                                createdAt = date.atStartOfDay(),
                            ),
                            TimeCapsule(
                                id = "dummy-id",
                                status = STATUS.LOCKED,
                                title = "오늘 아침에 친구를 만났는데, 친구가 늦었어..",
                                summary = "",
                                emotions =
                                    listOf(
                                        Emotion(
                                            emoji = "\uD83D\uDE14",
                                            label = "서운함",
                                            percentage = 30.0f
                                        ),
                                        Emotion(
                                            emoji = "\uD83D\uDE0A",
                                            label = "고마움",
                                            percentage = 30.0f
                                        ),
                                        Emotion(
                                            emoji = "\uD83E\uDD70",
                                            label = "안정감",
                                            percentage = 80.0f
                                        ),
                                    ),
                                createdAt = date.atStartOfDay(),
                                arriveAt = date.plusDays(10).atStartOfDay(),
                            ),
                            TimeCapsule(
                                id = "dummy-id",
                                status = STATUS.ARRIVED,
                                title = "오늘 아침에 친구를 만났는데, 친구가 늦었어..",
                                summary = "",
                                emotions =
                                    listOf(
                                        Emotion(
                                            emoji = "\uD83D\uDE14",
                                            label = "서운함",
                                            percentage = 30.0f
                                        ),
                                        Emotion(
                                            emoji = "\uD83D\uDE0A",
                                            label = "고마움",
                                            percentage = 30.0f
                                        ),
                                        Emotion(
                                            emoji = "\uD83E\uDD70",
                                            label = "안정감",
                                            percentage = 80.0f
                                        ),
                                    ),
                                createdAt = date.atStartOfDay(),
                                arriveAt = date.minusDays(15).atStartOfDay(),
                            ),
                            TimeCapsule(
                                id = "dummy-id",
                                status = STATUS.OPENED,
                                title = "오늘 아침에 친구를 만났는데, 친구가 늦었어..",
                                summary = "",
                                emotions =
                                    listOf(
                                        Emotion(
                                            emoji = "\uD83D\uDE14",
                                            label = "서운함",
                                            percentage = 30.0f
                                        ),
                                        Emotion(
                                            emoji = "\uD83D\uDE0A",
                                            label = "고마움",
                                            percentage = 30.0f
                                        ),
                                        Emotion(
                                            emoji = "\uD83E\uDD70",
                                            label = "안정감",
                                            percentage = 80.0f
                                        ),
                                    ),
                                createdAt = date.atStartOfDay(),
                                arriveAt = date.minusDays(15).atStartOfDay(),
                            ),
                        ),
                ),
            )
            delay(1000)
            emit(DataState.Loading(isLoading = false))
        }
}
