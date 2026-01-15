package com.emotionstorage.alarm.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emotionstorage.common.timeAgo
import com.emotionstorage.domain.model.Notification
import com.emotionstorage.domain.model.NotificationType
import com.emotionstorage.ui.theme.MooiTheme
import com.emotionstorage.ui.R
import java.time.LocalDateTime

@Composable
fun PushAlarmCard(
    title: String,
    body: String,
    arrivedAt: LocalDateTime,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Card(
        modifier =
            modifier
                .widthIn(328.dp)
                .heightIn(84.dp)
                .clip(RoundedCornerShape(15.dp))
                .clickable(onClick = onClick),
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(MooiTheme.colorScheme.secondaryBlue700.copy(alpha = 0.1f)),
    ) {
        Row(
            modifier =
                Modifier
                    .padding(start = 17.dp, end = 17.dp, top = 18.dp),
        ) {
            Icon(
                modifier =
                    Modifier
                        .size(20.dp)
                        .offset(y = 2.dp),
                painter = painterResource(id = R.drawable.ic_info),
                contentDescription = "알림 아이콘",
                tint = MooiTheme.colorScheme.gray600,
            )

            Spacer(modifier = Modifier.size(10.dp))
            Column(
                modifier =
                    Modifier
                        .align(Alignment.CenterVertically)
                        .weight(1f),
                verticalArrangement = Arrangement.Center,
            ) {
                Box(
                    modifier = Modifier.height(24.dp),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    Text(
                        text = title,
                        style = MooiTheme.typography.caption2,
                        color = Color.White,
                        maxLines = 1,
                    )
                }

                Box(
                    modifier = Modifier.height(24.dp),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    Text(
                        text = arrivedAt.timeAgo(),
                        style = MooiTheme.typography.caption7,
                        color = MooiTheme.colorScheme.gray600,
                        maxLines = 1,
                    )
                }
            }
            Icon(
                modifier =
                    Modifier
                        .align(Alignment.CenterVertically),
                painter = painterResource(R.drawable.ic_big_arrow_front),
                contentDescription = "상세 보기",
                tint = MooiTheme.colorScheme.gray300,
            )
        }
    }
}

@Preview
@Composable
fun PushAlarmCardPreview() {
    MooiTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(13.dp),
        ) {
            PushAlarmCard(
                title = "어제의 나, 리포트로 돌아왔어요",
                body = "하루의 마음 여정을 한눈에 만나보세요.",
                arrivedAt = LocalDateTime.now().minusHours(2),
            )

            PushAlarmCard(
                title = "기다리던 타임캡슐 도착!",
                body = "잠들어 있던 감정이 깨어났어요.",
                arrivedAt = LocalDateTime.now().minusDays(3),
            )
        }
    }
}
