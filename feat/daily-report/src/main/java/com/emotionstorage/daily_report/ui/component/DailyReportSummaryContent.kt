package com.emotionstorage.daily_report.ui.component

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun DailyReportSummaries(
    modifier: Modifier = Modifier,
    summaries: List<String> = emptyList(),
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        Text(
            text = "오늘 있었던 일은...",
            style = MooiTheme.typography.body2,
            color = MooiTheme.colorScheme.primary,
        )

        Column(
            modifier =
                Modifier
                    .background(Color(0x0AAECBFA), RoundedCornerShape(15.dp))
                    .border(1.dp, Color(0x33849BEA), RoundedCornerShape(15.dp))
                    .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            for (summary in summaries) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(11.dp),
                ) {
                    Box(
                        modifier = Modifier.padding(top = 5.dp),
                    ) {
                        Box(
                            modifier =
                                Modifier
                                    .size(5.dp)
                                    .background(Color.White, CircleShape),
                        )
                    }
                    Text(
                        text = summary,
                        style = MooiTheme.typography.caption3.copy(lineHeight = 22.sp),
                        color = Color.White,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun DailyReportSummariesPreview() {
    MooiTheme {
        Box(
            modifier = Modifier
                .padding(20.dp)
                .background(MooiTheme.colorScheme.background)
        )
        DailyReportSummaries(
            summaries =
                listOf(
                    "아침에 출근길에 친구와 같이 출근하기로 했는데 친구가 지각해놓고 미안하단말을 하지 않아 기분이 좋지 않았어요.",
                    "점심시간에는 동료와 업무 아이디어를 나누며 성취감을 느꼈고, 오후에는 팀 회의에서 의견이 무시당해 속상했어요.",
                    "퇴근길에는 카페에서 혼자 시간을 보내며 마음을 다독였고, 집에서는 고양이와 시간을 보내며 차분해졌어요.",
                ),
        )
    }
}

