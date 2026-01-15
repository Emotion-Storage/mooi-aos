package com.emotionstorage.alarm.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.emotionstorage.alarm.presentation.PushNotificationViewModel
import com.emotionstorage.alarm.ui.component.EmptyPushHolder
import com.emotionstorage.alarm.ui.component.PushAlarmCard
import com.emotionstorage.domain.model.Notification
import com.emotionstorage.domain.model.NotificationType
import com.emotionstorage.ui.R
import com.emotionstorage.ui.annotation.PreviewScreenRatios
import com.emotionstorage.ui.component.appBar.TopAppBar
import com.emotionstorage.ui.theme.MooiTheme
import java.time.LocalDateTime

@Composable
fun PushNotificationScreen(
    viewModel: PushNotificationViewModel = hiltViewModel(),
    navToTimeCapsuleDetail: (Long) -> Unit = {},
    navToDailyReportDetail: (Long) -> Unit = {},
    navToBack: () -> Unit = {},
) {
    val state = viewModel.state.collectAsState()

    StatelessPushNotificationScreen(
        value = state.value,
        navToBack = navToBack,
        navToTimeCapsuleDetail = navToTimeCapsuleDetail,
        navToDailyReportDetail = navToDailyReportDetail,
    )
}

@Composable
private fun StatelessPushNotificationScreen(
    value: List<Notification> = emptyList(),
    navToBack: () -> Unit,
    navToTimeCapsuleDetail: (Long) -> Unit,
    navToDailyReportDetail: (Long) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = "알림 내역",
                showBackButton = true,
                onBackClick = navToBack,
            )
        },
    ) { innerPadding ->
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(color = MooiTheme.colorScheme.backgroundDefault)
                    .padding(innerPadding),
        ) {
            Column(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 12.dp),
            ) {
                Row(
                    modifier =
                        Modifier
                            .height(24.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        modifier =
                            Modifier
                                .size(18.dp),
                        painter = painterResource(R.drawable.ic_alarm),
                        contentDescription = "알림 아이콘",
                        tint = MooiTheme.colorScheme.gray600,
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = "최근 3주간의 알림이 표시됩니다.",
                        style = MooiTheme.typography.caption7,
                        color = MooiTheme.colorScheme.gray500,
                    )
                }

                if (value.isEmpty()) {
                    EmptyPushHolder()
                } else {
                    LazyColumn(
                        modifier = Modifier.padding(top = 22.dp),
                    ) {
                        itemsIndexed(items = value, key = { _, item -> item.id }) { index, item ->
                            PushAlarmCard(
                                title = item.title,
                                body = item.body,
                                arrivedAt = item.arrivedAt,
                                onClick = {
                                    // todo: add error toast, if error occurs on navigation
                                    when (item.type) {
                                        is NotificationType.TimeCapsuleArrival -> {
                                            navToTimeCapsuleDetail((item.type as NotificationType.TimeCapsuleArrival).timeCapsuleId)
                                        }

                                        is NotificationType.DailyReportArrival -> {
                                            navToDailyReportDetail((item.type as NotificationType.DailyReportArrival).dailyReportId)
                                        }

                                        else -> {
                                            // do nothing
                                        }
                                    }
                                }
                            )
                            if (index < value.size - 1) Spacer(modifier = Modifier.size(12.dp))
                        }
                    }
                }
            }
        }
    }
}


@PreviewScreenRatios
@Composable
fun EmptyPushNotificationScreenPreview() {
    MooiTheme {
        StatelessPushNotificationScreen(
            value = listOf(),
            navToBack = {},
            navToTimeCapsuleDetail = {},
            navToDailyReportDetail = {},
        )
    }
}


@PreviewScreenRatios
@Composable
fun PushNotificationScreenPreview() {
    MooiTheme {
        StatelessPushNotificationScreen(
            value = listOf(
                Notification(
                    id = 0,
                    type = NotificationType.RecordSchedule,
                    title = "오늘 어떻게 보냈어요?",
                    body = "오늘 있었던 일, 아무거나 들어줄게요.",
                    arrivedAt = LocalDateTime.now().minusHours(1),
                ),
                Notification(
                    id = 1,
                    type = NotificationType.DailyReportArrival(1),
                    title = "어제의 나, 리포트로 돌아왔어요",
                    body = "하루의 마음 여정을 한눈에 만나보세요.",
                    arrivedAt = LocalDateTime.now().minusHours(3),
                ),
                Notification(
                    id = 2,
                    type = NotificationType.TimeCapsuleArrival(1),
                    title = "기다리던 타임캡슐 도착!",
                    body = "잠들어 있던 감정이 깨어났어요.",
                    arrivedAt = LocalDateTime.now().minusDays(0),
                ),
                Notification(
                    id = 3,
                    type = NotificationType.RecordReminder,
                    title = "우리 못본지 오래된 것 같아요...",
                    body = "00님의 안부가 궁금해요.",
                    arrivedAt = LocalDateTime.now().minusDays(3),
                )
            ),
            navToBack = {},
            navToTimeCapsuleDetail = {},
            navToDailyReportDetail = {},
        )
    }
}
