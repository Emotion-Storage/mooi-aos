package com.emotionstorage.daily_report.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun DailyReportEmotionScores(
    modifier: Modifier = Modifier,
    scores: List<Pair<String, Int>> = emptyList(),
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        Text(
            text = "감정 수치 종합 분석",
            style = MooiTheme.typography.body2,
            color = Color.White,
        )

        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .widthIn(max = 360.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            for ((label, score) in scores) {
                Row {
                    Text(
                        text = label + " ",
                        style = MooiTheme.typography.body5,
                        color = Color.White,
                    )
                    Text(
                        text = score.toString(),
                        style = MooiTheme.typography.body5,
                        color = MooiTheme.colorScheme.secondary,
                    )
                }
                ScoreSteps(
                    score = score,
                )
            }
        }
    }
}

@Composable
private fun ScoreSteps(
    modifier: Modifier = Modifier,
    score: Int,
) {
    //    0칸 : 0
    //    1칸 : 1~20
    //    2칸 : 21~40
    //    3칸 : 41~60
    //    4칸 : 61~80
    //    5칸 : 81~100

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        (0..4).toList().forEach {
            Box(
                modifier =
                    Modifier
                        .weight(1f)
                        .height(12.dp)
                        .background(
                            if (score > 0 && score > it * 20) {
                                MooiTheme.colorScheme.secondary
                            } else {
                                MooiTheme.colorScheme.gray300
                            },
                            RoundedCornerShape(
                                topStart = if (it == 0) 50.dp else 0.dp,
                                bottomStart = if (it == 0) 50.dp else 0.dp,
                                topEnd = if (it == 4) 50.dp else 0.dp,
                                bottomEnd = if (it == 4) 50.dp else 0.dp,
                            ),
                        ),
            )
        }
    }
}

@Preview
@Composable
private fun ScoreStepsPreview() {
    MooiTheme {
        Column(
            modifier =
                Modifier
                    .padding(20.dp)
                    .background(MooiTheme.colorScheme.background),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            ScoreSteps(score = 0)
            ScoreSteps(score = 20)
            ScoreSteps(score = 21)
            ScoreSteps(score = 40)
            ScoreSteps(score = 41)
            ScoreSteps(score = 60)
            ScoreSteps(score = 61)
            ScoreSteps(score = 80)
            ScoreSteps(score = 81)
            ScoreSteps(score = 100)
        }
    }
}

@Preview
@Composable
private fun DailyReportEmotionScoresPreview() {
    MooiTheme {
        Box(
            modifier =
                Modifier
                    .padding(20.dp)
                    .background(MooiTheme.colorScheme.background),
        ) {
            DailyReportEmotionScores(
                scores =
                    listOf(
                        "스트레스 지수" to 46,
                        "행복 지수" to 82,
                    ),
            )
        }
    }
}
