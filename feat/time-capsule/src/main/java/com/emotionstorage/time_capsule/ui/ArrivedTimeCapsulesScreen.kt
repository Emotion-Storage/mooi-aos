package com.emotionstorage.time_capsule.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
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
import com.emotionstorage.domain.model.TimeCapsule.Emotion
import com.emotionstorage.time_capsule.ui.component.TimeCapsuleItem
import com.emotionstorage.time_capsule.ui.model.TimeCapsuleState
import com.emotionstorage.ui.R
import com.emotionstorage.ui.component.TopAppBar
import com.emotionstorage.ui.theme.MooiTheme
import java.time.LocalDateTime

private val DUMMY_TIME_CAPSULES = (1..15).toList().map { it ->
    TimeCapsuleState(
        id = it.toString(),
        title = "오늘 아침에 친구를 만났는데, 친구가 늦었어..",
        emotions = listOf(
            Emotion(
                label = "서운함",
                icon = 0,
            ), Emotion(
                label = "화남",
                icon = 1,
            ), Emotion(
                label = "피곤함",
                icon = 2,
            )
        ),
        isFavorite = true,
        isFavoriteAt = LocalDateTime.now(),
        createdAt = LocalDateTime.now()
    )
}

@Composable
fun ArrivedTimeCapsulesScreen(
    modifier: Modifier = Modifier,
    navToTimeCapsuleDetail: (id: String) -> Unit = {},
    navToBack: () -> Unit = {}
) {
    StatelessArrivedTimeCapsulesScreen(
        modifier = modifier,
        timeCapsules = DUMMY_TIME_CAPSULES,
        navToTimeCapsuleDetail = navToTimeCapsuleDetail,
        navToBack = navToBack
    )
}

@Composable
private fun StatelessArrivedTimeCapsulesScreen(
    modifier: Modifier = Modifier,
    timeCapsules: List<TimeCapsuleState> = emptyList(),
    navToTimeCapsuleDetail: (id: String) -> Unit = {},
    navToBack: () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(MooiTheme.colorScheme.background), topBar = {
            TopAppBar(title = "도착한 타임캡슐", showBackButton = true, onBackClick = navToBack)
        }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MooiTheme.colorScheme.background)
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            // info text
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 13.dp, bottom = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.lock_open),
                    modifier = Modifier
                        .width(11.dp)
                        .height(12.dp),
                    contentDescription = "arrived",
                    colorFilter = ColorFilter.tint(MooiTheme.colorScheme.gray500)
                )
                Text(
                    text = "최근 3주간 도착한 타임캡슐입니다.",
                    style = MooiTheme.typography.body5,
                    color = MooiTheme.colorScheme.gray500
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .scrollable(scrollState, orientation = Orientation.Vertical)
            ) {
                items(items = timeCapsules, key = { it.id }) {
                    TimeCapsuleItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 17.dp),
                        timeCapsule = it,
                        onClick = { navToTimeCapsuleDetail(it.id) }
                    )
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
            timeCapsules = DUMMY_TIME_CAPSULES
        )
    }
}