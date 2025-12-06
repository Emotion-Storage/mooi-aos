package com.emotionstorage.time_capsule.ui.component.timeCapsuleItem

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emotionstorage.domain.model.TimeCapsule
import com.emotionstorage.domain.model.TimeCapsule.Emotion
import com.emotionstorage.time_capsule.ui.model.TimeCapsuleItemState
import com.emotionstorage.ui.R
import com.emotionstorage.ui.theme.MooiTheme


@Composable
fun TimeCapsuleItemContent(
    timeCapsule: TimeCapsuleItemState,
    modifier: Modifier = Modifier,
    blurContent: Boolean = false,
) {
    Row(
        modifier =
            modifier
                .fillMaxSize()
                .background(
                    Color(0x1A849BEA),
                    RoundedCornerShape(15.dp),
                )
                .run {
                    // blur content if not opened
                    if (blurContent) {
                        this.blur(4.dp)
                    } else {
                        this
                    }
                }
                .padding(top = 18.dp, bottom = 20.dp, start = 15.dp, end = 9.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(11.dp),
    ) {
        // unlocked icon
        if (timeCapsule.status == TimeCapsule.Status.OPENED) {
            Column(
                modifier =
                    Modifier
                        .size(54.dp)
                        .border(1.dp, Color(0xFFAECBFA).copy(alpha = 0.2f), CircleShape),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_lock_open),
                    modifier =
                        Modifier
                            .width(11.dp)
                            .height(14.dp),
                    contentDescription = "open",
                )
                Text(
                    modifier = Modifier.padding(top = 3.dp),
                    text = "열림",
                    style = MooiTheme.typography.caption6.copy(fontSize = 11.sp),
                    color = MooiTheme.colorScheme.secondary,
                )
            }
        }

        // time capsule content
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                for (emotion in timeCapsule.emotions) {
                    EmotionTag(emotion = emotion)
                }
            }
            Text(
                text = timeCapsule.title,
                style = MooiTheme.typography.caption2,
                color = MooiTheme.colorScheme.primary,
                maxLines = 1,
            )
        }
    }
}

@Composable
private fun EmotionTag(
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
            style = MooiTheme.typography.caption3,
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
