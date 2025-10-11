package com.emotionstorage.daily_report.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun DailyReportKeywords(
    modifier: Modifier = Modifier,
    keywords: List<String> = emptyList(),
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        Text(
            text = "오늘의 주요 키워드",
            style = MooiTheme.typography.body2,
            color = Color.White,
        )
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            for (keyword in keywords) {
                Box(
                    modifier = Modifier
                        .background(
                            Color(0xFFAECBFA).copy(alpha = 0.1f),
                            RoundedCornerShape(10.dp)
                        )
                        .padding(vertical = 15.dp, horizontal = 20.dp)
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = keyword,
                        style = MooiTheme.typography.body8,
                        color = MooiTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun DailyReportKeywordsPreview() {
    MooiTheme {
        Box(
            modifier = Modifier
                .padding(20.dp)
                .background(MooiTheme.colorScheme.background)
        ) {
            DailyReportKeywords(
                keywords =
                    listOf(
                        "친구의 지각",
                        "업무 아이디어",
                        "회의 스트레스",
                        "반려 동물",
                        "회복시간",
                    ),
            )
        }
    }
}

