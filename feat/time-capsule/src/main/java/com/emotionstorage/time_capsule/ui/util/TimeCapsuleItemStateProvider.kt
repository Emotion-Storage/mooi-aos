package com.emotionstorage.time_capsule.ui.util

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.emotionstorage.domain.model.TimeCapsule
import com.emotionstorage.domain.model.TimeCapsule.Emotion
import com.emotionstorage.time_capsule.ui.model.TimeCapsuleItemState
import java.time.LocalDateTime

internal class TimeCapsuleItemStateProvider(
    sampleState: TimeCapsuleItemState =
        TimeCapsuleItemState(
            id = 0L,
            status = TimeCapsule.Status.TEMPORARY,
            title = "오늘 아침에 친구를 만났는데, 친구가 늦었어..",
            emotions =
                listOf(
                    Emotion(
                        emoji = "\uD83D\uDE14",
                        label = "서운함",
                        percentage = 30.0f,
                    ),
                    Emotion(
                        emoji = "\uD83D\uDE0A",
                        label = "고마움",
                        percentage = 30.0f,
                    ),
                    Emotion(
                        emoji = "\uD83E\uDD70",
                        label = "안정감",
                        percentage = 80.0f,
                    ),
                ),
            createdAt = LocalDateTime.now(),
            expireAt = LocalDateTime.now().plusHours(3).plusMinutes(25),
            openDDay = null,
        ),
) : PreviewParameterProvider<TimeCapsuleItemState> {
    override val values =
        sequenceOf<TimeCapsuleItemState>(
            sampleState,
            sampleState.copy(
                status = TimeCapsule.Status.LOCKED,
                openDDay = 3,
            ),
            sampleState.copy(
                status = TimeCapsule.Status.ARRIVED,
                openDDay = 2,
            ),
            sampleState.copy(
                status = TimeCapsule.Status.OPENED,
            ),
        )
}
