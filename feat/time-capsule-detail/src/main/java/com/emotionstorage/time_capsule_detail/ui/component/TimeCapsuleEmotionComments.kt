package com.emotionstorage.time_capsule_detail.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emotionstorage.domain.model.TimeCapsule
import com.emotionstorage.ui.annotation.PreviewScreenRatios
import com.emotionstorage.ui.theme.MooiTheme
import com.emotionstorage.ui.util.LinearGradient

@Composable
fun TimeCapsuleEmotionComments(
    modifier: Modifier = Modifier,
    emotions: List<TimeCapsule.Emotion> = emptyList(),
    comments: List<String> = emptyList(),
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(27.dp),
    ) {
        Text(
            text = "내가 느낀 감정은\n아래와 같이 분석할 수 있어요.",
            style = MooiTheme.typography.body1,
            textAlign = TextAlign.Center,
            color = MooiTheme.colorScheme.primaryBlue500,
        )
        Emotions(emotions = emotions)
        Comments(comments = comments)
    }
}

@Composable
private fun Emotions(
    modifier: Modifier = Modifier,
    emotions: List<TimeCapsule.Emotion> = emptyList(),
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        emotions.forEach { emotion ->
            EmotionRowItem(
                modifier = Modifier.fillMaxWidth(),
                emotion = emotion,
            )
        }
    }
}

@Composable
private fun EmotionRowItem(
    emotion: TimeCapsule.Emotion,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .background(
                    LinearGradient(
                        listOf(
                            Color(0xFF849BEA).copy(alpha = 0.20f),
                            Color(0xFF4A5784).copy(alpha = 0.05f),
                        ),
                        angleInDegrees = -18f,
                    ),
                    shape = RoundedCornerShape(50.dp),
                ).padding(start = 20.dp, end = 28.dp, top = 18.dp, bottom = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = emotion.emoji,
            style = MooiTheme.typography.head2,
        )

        Spacer(modifier = Modifier.size(12.dp))

        Text(
            modifier = Modifier.weight(1f),
            text = emotion.label,
            style = MooiTheme.typography.body5,
            color = MooiTheme.colorScheme.primaryBlue500,
            maxLines = 1,
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = "${emotion.percentage?.toInt() ?: "-"}%",
            style = MooiTheme.typography.body6,
            color = Color.White,
        )
    }
}

@Composable
private fun Comments(
    modifier: Modifier = Modifier,
    comments: List<String> = emptyList(),
) {
    Column(
        modifier =
            modifier
                .background(Color(0x0AAECBFA), RoundedCornerShape(15.dp))
                .border(1.dp, Color(0x33849BEA), RoundedCornerShape(15.dp))
                .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        for (comment in comments) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = comment,
                style = MooiTheme.typography.caption3.copy(lineHeight = 22.sp),
                color = Color.White,
            )
        }
    }
}

@PreviewScreenRatios
@Composable
private fun TimeCapsuleEmotionCommentsPreview() {
    MooiTheme {
        Column(
            modifier =
                Modifier
                    .background(MooiTheme.colorScheme.backgroundDefault)
                    .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Emotions(
                emotions =
                    listOf(
                        TimeCapsule.Emotion(
                            emoji = "\uD83D\uDE14",
                            label = "가나다라",
                            percentage = 30.0f,
                        ),
                        TimeCapsule.Emotion(
                            emoji = "\uD83D\uDE0A",
                            label = "가나다라마",
                            percentage = 30.0f,
                        ),
                        TimeCapsule.Emotion(
                            emoji = "\uD83E\uDD70",
                            label = "가나다라마바",
                            percentage = 80.0f,
                        ),
                    ),
            )

            TimeCapsuleEmotionComments(
                emotions =
                    listOf(
                        TimeCapsule.Emotion(
                            emoji = "\uD83D\uDE14",
                            label = "가",
                            percentage = 30.0f,
                        ),
                        TimeCapsule.Emotion(
                            emoji = "\uD83D\uDE0A",
                            label = "가나",
                            percentage = 30.0f,
                        ),
                        TimeCapsule.Emotion(
                            emoji = "\uD83E\uDD70",
                            label = "가나다",
                            percentage = 80.0f,
                        ),
                    ),
                comments =
                    listOf(
                        "오늘은 조금 힘든 일이 있었지만, 가족과의 따뜻한 시간 덕분에 긍정적인 감정으로 마무리했어요.",
                        "귀가 후 가족애와 안정감을 느끼면서, 부정적 감정을 회복할 수 있었어요.",
                        "감정이 복잡하게 얽힌 하루였네요. 하지만 작은 부분에서 감사함을 느끼는 모습이 멋져요.",
                    ),
            )
        }
    }
}
