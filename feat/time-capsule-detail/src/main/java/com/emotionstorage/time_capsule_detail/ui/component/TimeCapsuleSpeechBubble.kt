package com.emotionstorage.time_capsule_detail.ui.component

import SpeechBubble
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.emotionstorage.ui.R
import com.emotionstorage.ui.theme.MooiTheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun TimeCapsuleSpeechBubble(
    createdAt: LocalDateTime,
    saveAt: LocalDateTime,
    modifier: Modifier = Modifier,
    isNewTimeCapsule: Boolean = false,
    arriveAt: LocalDateTime? = null,
    emotions: List<String> = emptyList(),
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        if (arriveAt != null) {
            Row(
                modifier = Modifier.height(40.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(3.dp),
            ) {
                Image(
                    modifier = Modifier.size(11.dp, 14.dp),
                    painter = painterResource(R.drawable.lock),
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(MooiTheme.colorScheme.primary),
                )
                Text(
                    text = "${arriveAt.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"))} 에 열릴 예정이에요!",
                    style = MooiTheme.typography.body6,
                    color = MooiTheme.colorScheme.primary,
                )
            }
        }

        SpeechBubble(
            sizeParam = DpSize(
                286.dp,
                if (isNewTimeCapsule) 83.dp else 115.5.dp
            ),
            cornerRadius = 100.dp,
        ) {
            Column(
                modifier = Modifier,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                if (!isNewTimeCapsule) {
                    Row(
                        modifier = Modifier,
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Image(
                            modifier = Modifier.size(14.dp),
                            painter = painterResource(R.drawable.pencil),
                            contentDescription = "",
                        )
                        Text(
                            text = "감정 기록일 : ${createdAt.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"))}",
                            style = MooiTheme.typography.caption3,
                            color = MooiTheme.colorScheme.gray300,
                        )
                    }
                }
                Row(
                    modifier = Modifier,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Image(
                        modifier = Modifier.size(14.dp),
                        painter = painterResource(R.drawable.clock),
                        contentDescription = "",
                    )
                    Text(
                        text = "보관일 : ${saveAt.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"))}",
                        style = MooiTheme.typography.caption3,
                        color = MooiTheme.colorScheme.gray300,
                    )
                }

                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    for (emotion in emotions) {
                        val (emoji, label) = emotion.split(" ")
                        Row(
                            modifier =
                                Modifier
                                    .height(27.dp)
                                    .border(1.dp, Color(0xFFAECBFA).copy(alpha = 0.2f), RoundedCornerShape(20.dp))
                                    .padding(horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                        ) {
                            Text(text = emoji, style = MooiTheme.typography.body5)
                            Text(
                                text = label,
                                style = MooiTheme.typography.caption3,
                                color = MooiTheme.colorScheme.primary,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun TimeCapsuleSpeeckBubblePreview() {
    MooiTheme {
        Column(
            modifier =
                Modifier
                    .background(MooiTheme.colorScheme.background)
                    .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            TimeCapsuleSpeechBubble(
                isNewTimeCapsule = true,
                createdAt = LocalDateTime.now(),
                saveAt = LocalDateTime.now(),
                emotions = listOf("\uD83D\uDE14 서운함", "\uD83D\uDE0A 고마움", "\uD83E\uDD70 안정감"),
            )
            TimeCapsuleSpeechBubble(
                isNewTimeCapsule = true,
                createdAt = LocalDateTime.now(),
                saveAt = LocalDateTime.now(),
                arriveAt = LocalDateTime.now(),
                emotions = listOf("\uD83D\uDE14 서운함", "\uD83D\uDE0A 고마움", "\uD83E\uDD70 안정감"),
            )
            TimeCapsuleSpeechBubble(
                createdAt = LocalDateTime.now().minusHours(3),
                saveAt = LocalDateTime.now(),
                emotions = listOf("\uD83D\uDE14 서운함", "\uD83D\uDE0A 고마움", "\uD83E\uDD70 안정감"),
            )
            TimeCapsuleSpeechBubble(
                createdAt = LocalDateTime.now().minusHours(3),
                saveAt = LocalDateTime.now(),
                arriveAt = LocalDateTime.now(),
                emotions = listOf("\uD83D\uDE14 서운함", "\uD83D\uDE0A 고마움", "\uD83E\uDD70 안정감"),
            )
        }
    }
}
