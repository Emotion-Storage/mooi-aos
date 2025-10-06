package com.emotionstorage.time_capsule.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emotionstorage.domain.model.TimeCapsule
import com.emotionstorage.domain.model.TimeCapsule.Emotion
import com.emotionstorage.time_capsule.ui.component.TimeCapsuleItem
import com.emotionstorage.time_capsule.ui.model.TimeCapsuleItemState
import com.emotionstorage.ui.R
import com.emotionstorage.ui.component.TopAppBar
import com.emotionstorage.ui.theme.MooiTheme
import java.time.LocalDateTime

private val DUMMY_TIME_CAPSULES =
    (1..15).toList().map { it ->
        TimeCapsuleItemState(
            id = it.toString(),
            status = TimeCapsule.STATUS.ARRIVED,
            title = "오늘 아침에 친구를 만났는데, 친구가 늦었어..",
            emotions =
                listOf(
                    Emotion(
                        label = "서운함",
                        icon = 0,
                    ),
                    Emotion(
                        label = "화남",
                        icon = 1,
                    ),
                    Emotion(
                        label = "피곤함",
                        icon = 2,
                    ),
                ),
            isFavorite = true,
            isFavoriteAt = LocalDateTime.now(),
            createdAt = LocalDateTime.now(),
            expireAt = LocalDateTime.now().plusHours(5),
            openDDay = it,
        )
    }

@Composable
fun ArrivedTimeCapsulesScreen(
    modifier: Modifier = Modifier,
    navToTimeCapsuleDetail: (id: String) -> Unit = {},
    navToBack: () -> Unit = {},
) {
    StatelessArrivedTimeCapsulesScreen(
        modifier = modifier,
        timeCapsules = DUMMY_TIME_CAPSULES,
        navToTimeCapsuleDetail = navToTimeCapsuleDetail,
        navToBack = navToBack,
    )
}

@Composable
private fun StatelessArrivedTimeCapsulesScreen(
    modifier: Modifier = Modifier,
    timeCapsules: List<TimeCapsuleItemState> = emptyList(),
    navToTimeCapsuleDetail: (id: String) -> Unit = {},
    navToBack: () -> Unit = {},
) {
    val scrollState = rememberScrollState()

    Scaffold(
        modifier =
            modifier
                .fillMaxSize()
                .background(MooiTheme.colorScheme.background),
        topBar = {
            TopAppBar(title = "도착한 타임캡슐", showBackButton = true, onBackClick = navToBack)
        },
    ) { innerPadding ->
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(MooiTheme.colorScheme.background)
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp),
        ) {
            if (timeCapsules.isEmpty()) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "최근 도착한 타임캡슐이 없어요.",
                    style = MooiTheme.typography.caption2,
                    color = MooiTheme.colorScheme.gray400,
                )
            }

            Column(
                modifier = Modifier.align(Alignment.TopCenter).fillMaxWidth(),
            ) {
                // info text
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 13.dp, bottom = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.lock_open),
                        modifier =
                            Modifier
                                .width(12.dp)
                                .height(14.dp),
                        contentDescription = "arrived",
                        colorFilter = ColorFilter.tint(MooiTheme.colorScheme.gray500),
                    )
                    Text(
                        text = "최근 3주간 도착한 타임캡슐을 표시합니다.\n도착한 타임캡슐을 열어 내 지난 감정을 확인해보세요.",
                        style = MooiTheme.typography.caption7.copy(lineHeight = 22.sp),
                        color = MooiTheme.colorScheme.gray500,
                    )
                }

                LazyColumn(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .scrollable(scrollState, orientation = Orientation.Vertical),
                ) {
                    items(items = timeCapsules, key = { it.id }) {
                        TimeCapsuleItem(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 17.dp),
                            timeCapsule = it,
                            showDate = true,
                            showInfoText = false,
                            onClick = { navToTimeCapsuleDetail(it.id) },
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun ArrivedTimeCapsulesScreenPreview() {
    MooiTheme {
        StatelessArrivedTimeCapsulesScreen(
            timeCapsules = DUMMY_TIME_CAPSULES,
        )
    }
}

@Preview
@Composable
private fun EmptyArrivedTimeCapsulesScreenPreview() {
    MooiTheme {
        StatelessArrivedTimeCapsulesScreen(
            timeCapsules = emptyList(),
        )
    }
}
