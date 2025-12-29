package com.emotionstorage.time_capsule_detail.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emotionstorage.domain.model.TimeCapsule
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
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(11.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        for (emotion in emotions) {
            Box(
                modifier =
                    Modifier
                        .background(
                            LinearGradient(
                                listOf(
                                    // alpha = 0.5 * 0.2
                                    Color(0xFF849BEA).copy(alpha = 0.1f),
                                    // alpha = 0.08 * 0.2
                                    Color(0xFF849BEA).copy(alpha = 0.016f),
                                ),
                                angleInDegrees = -18f,
                            ),
                            RoundedCornerShape(10.dp),
                        )
                        .padding(vertical = 17.dp, horizontal = 18.dp),
            ) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                    ) {
                        Text(
                            text = emotion.emoji,
                            style = MooiTheme.typography.head2,
                        )
                        Text(
                            text = emotion.label,
                            style = MooiTheme.typography.body8,
                            color = MooiTheme.colorScheme.primaryBlue500,
                        )
                    }
                    Text(
                        text = "${emotion.percentage?.toInt() ?: "- "}%",
                        style = MooiTheme.typography.head3.copy(lineHeight = 29.sp),
                        color = Color.White,
                    )
                }
            }
        }
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


@Preview
@Composable
private fun EmotionsPreview() {
    val emotions =
        listOf(
            TimeCapsule.Emotion(
                emoji = "\uD83D\uDE14",
                label = "서운함",
                percentage = 30.0f,
            ),
            TimeCapsule.Emotion(
                emoji = "\uD83D\uDE0A",
                label = "고마움",
                percentage = 30.0f,
            ),
            TimeCapsule.Emotion(
                emoji = "\uD83E\uDD70",
                label = "안정감",
                percentage = 80.0f,
            ),
        )
    MooiTheme {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(MooiTheme.colorScheme.backgroundDefault)
                    .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Emotions(
                emotions = emotions,
            )
            Emotions(
                emotions = emotions.subList(0, 2),
            )
            Emotions(
                emotions = emotions.subList(0, 1),
            )
        }
    }
}

@Preview
@Composable
private fun TimeCapsuleEmotionCommentsPreview() {
    MooiTheme {
        TimeCapsuleEmotionComments(
            modifier = Modifier
                .background(MooiTheme.colorScheme.backgroundDefault)
                .padding(16.dp),
            emotions =
                listOf(
                    TimeCapsule.Emotion(
                        emoji = "\uD83D\uDE14",
                        label = "서운함",
                        percentage = 30.0f,
                    ),
                    TimeCapsule.Emotion(
                        emoji = "\uD83D\uDE0A",
                        label = "고마움",
                        percentage = 30.0f,
                    ),
                    TimeCapsule.Emotion(
                        emoji = "\uD83E\uDD70",
                        label = "안정감",
                        percentage = 80.0f,
                    )
                ),
            comments = listOf(
                "오늘은 조금 힘든 일이 있었지만, 가족과의 따뜻한 시간 덕분에 긍정적인 감정으로 마무리했어요.",
                "귀가 후 가족애와 안정감을 느끼면서, 부정적 감정을 회복할 수 있었어요.",
                "감정이 복잡하게 얽힌 하루였네요. 하지만 작은 부분에서 감사함을 느끼는 모습이 멋져요."
            )
        )
    }
}

