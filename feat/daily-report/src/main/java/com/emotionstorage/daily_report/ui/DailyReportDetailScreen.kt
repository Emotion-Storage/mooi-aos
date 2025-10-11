package com.emotionstorage.daily_report.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.emotionstorage.common.toKorDate
import com.emotionstorage.daily_report.presentation.DailyReportDetailAction
import com.emotionstorage.daily_report.presentation.DailyReportDetailState
import com.emotionstorage.daily_report.presentation.DailyReportDetailViewModel
import com.emotionstorage.daily_report.ui.component.DailyReportEmotionLog
import com.emotionstorage.daily_report.ui.component.DailyReportEmotionScores
import com.emotionstorage.daily_report.ui.component.DailyReportKeywords
import com.emotionstorage.daily_report.ui.component.DailyReportSummaries
import com.emotionstorage.domain.model.DailyReport
import com.emotionstorage.domain.model.DailyReport.EmotionLog
import com.emotionstorage.ui.component.FullLoadingScreen
import com.emotionstorage.ui.component.TopAppBar
import com.emotionstorage.ui.theme.MooiTheme
import java.time.LocalDateTime

@Composable
fun DailyReportDetailScreen(
    id: String,
    modifier: Modifier = Modifier,
    viewModel: DailyReportDetailViewModel = hiltViewModel(),
    navToBack: () -> Unit = {},
) {
    val state = viewModel.container.stateFlow.collectAsState()

    LaunchedEffect(id) {
        viewModel.onAction(DailyReportDetailAction.Init(id))
    }

    if (state.value.dailyReport == null) {
        FullLoadingScreen()
    } else {
        StatelessDailyReportDetailScreen(
            modifier = modifier,
            state = state.value,
            navToBack = navToBack,
        )
    }
}

@Composable
private fun StatelessDailyReportDetailScreen(
    modifier: Modifier = Modifier,
    state: DailyReportDetailState = DailyReportDetailState(),
    navToBack: () -> Unit = {},
) {
    val scrollState = rememberScrollState()

    Scaffold(
        modifier =
            modifier
                .fillMaxSize()
                .background(MooiTheme.colorScheme.background),
        topBar = {
            TopAppBar(
                title =
                    state
                        .dailyReport!!
                        .createdAt
                        .toLocalDate()
                        .toKorDate(),
                showBackButton = true,
                onBackClick = navToBack,
            )
        },
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(MooiTheme.colorScheme.background)
                    .padding(innerPadding)
                    .padding(top = 31.dp, bottom = 43.dp)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(scrollState),
        ) {
            DailyReportSummaries(
                summaries = state.dailyReport!!.summaries,
            )
            Spacer(modifier = Modifier.height(53.dp))
            DailyReportKeywords(
                keywords = state.dailyReport!!.keywords,
            )
            Spacer(modifier = Modifier.height(53.dp))
            DailyReportEmotionLog(
                emotionLogs = state.dailyReport!!.emotionLogs,
            )
            Spacer(modifier = Modifier.height(53.dp))
            DailyReportEmotionScores(
                scores =
                    listOf(
                        "스트레스 지수" to state.dailyReport!!.stressScore,
                        "행복 지수" to state.dailyReport!!.happinessScore,
                    ),
            )
            Spacer(modifier = Modifier.height(53.dp))
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color(0xFF0E0C12).copy(alpha = 0.5f),
                            shape = RoundedCornerShape(50),
                        ).padding(vertical = 15.dp, horizontal = 38.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "감정 요약",
                    style = MooiTheme.typography.body8,
                    color = MooiTheme.colorScheme.primary,
                )
                Text(
                    text = state.dailyReport!!.emotionSummary,
                    style = MooiTheme.typography.body8,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                )
            }
            Spacer(modifier = Modifier.height(42.dp))
            Text(
                modifier = Modifier.fillMaxWidth(),
                text =
                    "하루의 대화를 바탕으로 작성된 일일리포트는\n" +
                        "당일 마지막 감정 대화 기준 24시간 후 업데이트 됩니다.",
                style = MooiTheme.typography.caption7.copy(fontWeight = FontWeight.Normal, lineHeight = 22.sp),
                color = MooiTheme.colorScheme.gray400,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Preview
@Composable
private fun DailyReportDetailScreenPreview() {
    MooiTheme {
        StatelessDailyReportDetailScreen(
            state =
                DailyReportDetailState(
                    dailyReport =
                        DailyReport(
                            id = "id",
                            summaries =
                                listOf(
                                    "아침에 출근길에 친구와 같이 출근하기로 했는데 친구가 지각해놓고 미안하단말을 하지 않아 기분이 좋지 않았어요.",
                                    "점심시간에는 동료와 업무 아이디어를 나누며 성취감을 느꼈고, 오후에는 팀 회의에서 의견이 무시당해 속상했어요.",
                                    "퇴근길에는 카페에서 혼자 시간을 보내며 마음을 다독였고, 집에서는 고양이와 시간을 보내며 차분해졌어요.",
                                ),
                            keywords =
                                listOf(
                                    "친구의 지각",
                                    "업무 아이디어",
                                    "회의 스트레스",
                                    "반려 동물",
                                    "회복시간",
                                ),
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
                            createdAt = LocalDateTime.now(),
                            stressScore = 46,
                            happinessScore = 82,
                            emotionSummary = "하루종일 감정 기복은 있었지만, 하루의 끝은 안정감과 평온함으로 마무리 되었어요.",
                        ),
                ),
        )
    }
}

@Preview
@Composable
private fun DailyReportDetailScreenPreview2() {
    MooiTheme {
        StatelessDailyReportDetailScreen(
            state =
                DailyReportDetailState(
                    dailyReport =
                        DailyReport(
                            id = "id",
                            summaries =
                                listOf(
                                    "아침에 출근길에 친구와 같이 출근하기로 했는데 친구가 지각해놓고 미안하단말을 하지 않아 기분이 좋지 않았어요.",
                                ),
                            keywords =
                                listOf(
                                    "친구의 지각",
                                ),
                            emotionLogs =
                                listOf(
                                    EmotionLog(
                                        emoji = "\uD83D\uDE20",
                                        label = "짜증남",
                                        description = "친구의 지각",
                                        time = LocalDateTime.now(),
                                    ),
                                ),
                            createdAt = LocalDateTime.now(),
                            stressScore = 75,
                            happinessScore = 12,
                            emotionSummary = "작은 일로 시작된 불편한 기분이 쉽게 풀리지 않아,\n마음이 지친 하루였습니다.",
                        ),
                ),
        )
    }
}
