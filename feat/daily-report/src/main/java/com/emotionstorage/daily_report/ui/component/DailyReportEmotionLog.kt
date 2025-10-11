package com.emotionstorage.daily_report.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emotionstorage.common.formatToKorTime
import com.emotionstorage.domain.model.DailyReport.EmotionLog
import com.emotionstorage.ui.theme.MooiTheme
import com.emotionstorage.ui.util.LinearGradient
import java.time.LocalDateTime

@Composable
fun DailyReportEmotionLog(
    modifier: Modifier = Modifier,
    emotionLogs: List<EmotionLog> = emptyList(),
) {
    var logContentHeight by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        Text(
            text = "오늘의 감정",
            style = MooiTheme.typography.body2,
            color = Color.White,
        )

        Box(modifier = Modifier.padding(start = 5.dp)) {
            Box(
                modifier =
                    Modifier
                        .align(Alignment.CenterStart)
                        .offset(x = 12.dp)
                        .height(logContentHeight)
                        .width(10.dp)
                        .padding(vertical = 15.dp)
                        .blur(2.5.dp),
            ) {
                Box(
                    modifier =
                        Modifier
                            .align(Alignment.Center)
                            .fillMaxHeight()
                            .width(2.dp)
                            .background(MooiTheme.colorScheme.secondary.copy(alpha = 0.7f)),
                )
            }

            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .onGloballyPositioned {
                            logContentHeight =
                                with(density) {
                                    it.size.height.toDp()
                                }
                        },
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                emotionLogs.forEach { emotionLog ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = emotionLog.emoji,
                            style = TextStyle(fontSize = 28.sp),
                            color = Color.White,
                        )
                        Text(
                            modifier = Modifier.padding(start = 12.dp, end = 18.dp),
                            text = emotionLog.time.formatToKorTime(),
                            style = MooiTheme.typography.body8,
                            color = MooiTheme.colorScheme.gray500,
                        )
                        Row(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .background(
                                        LinearGradient(
                                            colors =
                                                listOf(
                                                    Color(0xFF849BEA).copy(alpha = 0.15f),
                                                    Color.Transparent,
                                                ),
                                        ),
                                        RoundedCornerShape(10.dp),
                                    ).padding(vertical = 7.dp, horizontal = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            Text(
                                text = emotionLog.label,
                                style = MooiTheme.typography.body5,
                                color = MooiTheme.colorScheme.primary,
                            )
                            Text(
                                text = emotionLog.description,
                                style = MooiTheme.typography.body5,
                                color = Color.White,
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
private fun DailyReportEmotionLogPreview() {
    MooiTheme {
        Box(
            modifier =
                Modifier
                    .padding(20.dp)
                    .background(MooiTheme.colorScheme.background),
        ) {
            DailyReportEmotionLog(
                emotionLogs =
                    listOf(
                        EmotionLog(
                            emoji = "\uD83D\uDE20",
                            label = "짜증남",
                            description = "친구의 지각",
                            time = LocalDateTime.now(),
                        ),
                        EmotionLog(
                            emoji = "\uD83D\uDE42",
                            label = "성취감",
                            description = "업무 아이디어",
                            time = LocalDateTime.now(),
                        ),
                        EmotionLog(
                            emoji = "\uD83D\uDE1E",
                            label = "서운함",
                            description = "팀 회의에서 무시당함",
                            time = LocalDateTime.now(),
                        ),
                        EmotionLog(
                            emoji = "\uD83D\uDE0C",
                            label = "안정감",
                            description = "카페에서 힐링",
                            time = LocalDateTime.now(),
                        ),
                        EmotionLog(
                            emoji = "\uD83D\uDE3A",
                            label = "따뜻함",
                            description = "고양이와의 시간",
                            time = LocalDateTime.now(),
                        ),
                    ),
            )
        }
    }
}

@Preview
@Composable
private fun DailyReportEmotionLogPreview2() {
    MooiTheme {
        Box(
            modifier =
                Modifier
                    .padding(20.dp)
                    .background(MooiTheme.colorScheme.background),
        ) {
            DailyReportEmotionLog(
                emotionLogs =
                    listOf(
                        EmotionLog(
                            emoji = "\uD83D\uDE20",
                            label = "짜증남",
                            description = "친구의 지각",
                            time = LocalDateTime.now(),
                        ),
                    ),
            )
        }
    }
}
