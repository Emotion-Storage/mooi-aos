package com.emotionstorage.time_capsule.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emotionstorage.domain.model.TimeCapsule.Emotion
import com.emotionstorage.ui.theme.MooiTheme
import com.emotionstorage.ui.util.getIconResId

@Composable
fun EmotionTag(
    modifier: Modifier = Modifier,
    emotion: Emotion
) {
    Row(
        modifier = modifier
            .height(24.dp)
            .background(Color(0xFFAECBFA).copy(alpha = 0.2f), RoundedCornerShape(20.dp))
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        if (emotion.getIconResId() == null) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .background(Color.Gray, CircleShape)
            )
        } else {
            Image(
                painter = painterResource(id = emotion.getIconResId()!!),
                modifier = Modifier.size(16.dp),
                contentDescription = emotion.label
            )
        }

        Text(
            text = emotion.label,
            style = MooiTheme.typography.body4,
            color = MooiTheme.colorScheme.primary
        )
    }
}

@Preview
@Composable
private fun EmotionTagPreview() {
    val emotions = listOf(
        Emotion(
            label = "서운함",
            icon = 0,
        ), Emotion(
            label = "화남",
            icon = 1,
        ), Emotion(
            label = "피곤함",
            icon = 2,
        )
    )

    MooiTheme {
        Row(
            modifier = Modifier.background(MooiTheme.colorScheme.background).padding(6.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (emotion in emotions) {
                EmotionTag(emotion = emotion)
            }
        }
    }
}

