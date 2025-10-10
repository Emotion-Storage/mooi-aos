package com.emotionstorage.daily_report.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.emotionstorage.common.toKorDate
import com.emotionstorage.daily_report.presentation.DailyReportDetailAction
import com.emotionstorage.daily_report.presentation.DailyReportDetailState
import com.emotionstorage.daily_report.presentation.DailyReportDetailViewModel
import com.emotionstorage.domain.model.DailyReport
import com.emotionstorage.domain.model.DailyReport.EmotionLog
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
        LoadingScreen(modifier = modifier)
    } else {
        StatelessDailyReportDetailScreen(
            modifier = modifier,
            state = state.value,
            navToBack = navToBack,
        )
    }
}

@Composable
private fun LoadingScreen(modifier: Modifier) {
    Scaffold(
        modifier =
            modifier
                .fillMaxSize()
                .background(MooiTheme.colorScheme.background),
    ) { innerPadding ->
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(MooiTheme.colorScheme.background)
                    .padding(innerPadding),
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
                    .padding(top = 31.dp, bottom = 55.dp)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(scrollState),
        ) {
            // todo: make screen ui
            Text(
                state.dailyReport.toString(),
                color = Color.White,
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
