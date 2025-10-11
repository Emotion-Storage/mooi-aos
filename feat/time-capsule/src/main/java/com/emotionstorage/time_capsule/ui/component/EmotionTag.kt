package com.emotionstorage.time_capsule.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emotionstorage.domain.model.TimeCapsule.Emotion
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun EmotionTag(
    modifier: Modifier = Modifier,
    emotion: Emotion,
) {
    Row(
        modifier =
            modifier
                .height(24.dp)
                .background(Color(0xFFAECBFA).copy(alpha = 0.2f), RoundedCornerShape(20.dp))
                .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = emotion.emoji,
            style = MooiTheme.typography.caption4,
        )
        Text(
            text = emotion.label,
            style = MooiTheme.typography.caption4,
            color = MooiTheme.colorScheme.primary,
        )
    }
}

@Preview
@Composable
private fun EmotionTagPreview() {
    val emotions =
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
        )

    MooiTheme {
        Row(
            modifier = Modifier.background(MooiTheme.colorScheme.background).padding(6.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            for (emotion in emotions) {
                EmotionTag(emotion = emotion)
            }
        }
    }
}
