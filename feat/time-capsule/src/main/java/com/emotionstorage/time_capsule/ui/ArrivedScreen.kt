package com.emotionstorage.time_capsule.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emotionstorage.common.getKorDayOfWeek
import com.emotionstorage.domain.model.TimeCapsule.Emotion
import com.emotionstorage.time_capsule.ui.component.EmotionTag
import com.emotionstorage.time_capsule.ui.model.ArrivedTimeCapsule
import com.emotionstorage.ui.R
import com.emotionstorage.ui.component.TopAppBar
import com.emotionstorage.ui.theme.MooiTheme
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import java.time.format.DateTimeFormatter

private val DUMMY_ARRIVED = (1..15).toList().map { it ->
    ArrivedTimeCapsule(
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
        arriveAt = LocalDateTime.of(
            2025, if (it < 10) 7 else 8, 21 + (it / 3), 9, 28
        ),
    )
}.sortedByDescending { it.arriveAt }

@Composable
fun ArrivedScreen(
    modifier: Modifier = Modifier,
    navToTimeCapsuleDetail: (id: String) -> Unit = {},
    navToBack: () -> Unit = {}
) {
    StatelessArrivedScreen(
        modifier = modifier,
        arrivedTimeCapsules = DUMMY_ARRIVED,
        navToTimeCapsuleDetail = navToTimeCapsuleDetail,
        navToBack = navToBack
    )
}

@Composable
private fun StatelessArrivedScreen(
    modifier: Modifier = Modifier,
    arrivedTimeCapsules: List<ArrivedTimeCapsule> = emptyList(),
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

            // arrived time capsules
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .scrollable(scrollState, orientation = Orientation.Vertical),
            ) {

                // caveat!! arrived time capsules must be sorted in descending order by arriveAt
                arrivedTimeCapsules.forEachIndexed { index, item ->
                    item(key = item.id) {
                        Column {
                            // year & month
                            if (index == 0 || arrivedTimeCapsules[index - 1].arriveAt.toLocalDate().month != item.arriveAt.toLocalDate().month) {
                                Text(
                                    modifier = Modifier.padding(bottom = 14.dp),
                                    text = "${item.arriveAt.year}년 ${item.arriveAt.monthValue}월",
                                    style = MooiTheme.typography.body2,
                                    color = Color.White
                                )
                            }

                            ArrivedTimeCapsule(
                                modifier = Modifier.fillMaxWidth(),
                                item = item,
                                showDate = index == 0 || arrivedTimeCapsules[index - 1].arriveAt.toLocalDate() != item.arriveAt.toLocalDate(),
                                isLastOfDate = index == arrivedTimeCapsules.lastIndex || item.arriveAt.toLocalDate() != arrivedTimeCapsules[index + 1].arriveAt.toLocalDate(),
                                isFirstDayOfMonth = index == 0 || arrivedTimeCapsules[index - 1].arriveAt.monthValue != item.arriveAt.monthValue,
                                isLastDayOfMonth = !arrivedTimeCapsules.any { it -> it.arriveAt.monthValue == item.arriveAt.monthValue && it.arriveAt < item.arriveAt },
                                onClick = { navToTimeCapsuleDetail(item.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun ArrivedTimeCapsule(
    item: ArrivedTimeCapsule,
    modifier: Modifier = Modifier,
    showDate: Boolean = false,
    isLastOfDate: Boolean = false,
    isFirstDayOfMonth: Boolean = false,
    isLastDayOfMonth: Boolean = false,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(if (isLastOfDate) 189.dp else 148.dp),
        horizontalArrangement = Arrangement.spacedBy(15.dp),
    ) {
        ArrivedTimeCapsuleDateLine(
            modifier = Modifier.fillMaxHeight(),
            date = item.arriveAt.toLocalDate(),
            showDate = showDate,
            isFirstDayOfMonth = isFirstDayOfMonth,
            isLastDayOfMonth = isLastDayOfMonth
        )


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MooiTheme.colorScheme.background),
        ) {
            // arrive time
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(33.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // todo: change date format
                Text(
                    text = item.arriveAt.format(DateTimeFormatter.ofPattern("hh:mm")),
                    style = MooiTheme.typography.body4.copy(fontWeight = FontWeight.Light),
                    color = MooiTheme.colorScheme.gray300
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.lock_open),
                        modifier = Modifier
                            .width(12.dp)
                            .height(14.dp),
                        contentDescription = "arrived",
                        colorFilter = ColorFilter.tint(MooiTheme.colorScheme.secondary)
                    )
                    Text(
                        text = "도착 D+${
                            Period.between(
                                item.arriveAt.toLocalDate(), LocalDate.now()
                            ).days
                        }",
                        style = MooiTheme.typography.body4,
                        color = MooiTheme.colorScheme.secondary
                    )
                }
            }

            // capsule content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(93.dp)
                    .background(
                        Color(0xFF849BEA).copy(alpha = 0.1f), RoundedCornerShape(15.dp)
                    )
                    .border(
                        1.dp, Color(0xFF849BEA).copy(alpha = 0.2f), RoundedCornerShape(15.dp)
                    )
                    .padding(vertical = 18.dp, horizontal = 15.dp)
                    .clickable(onClick = onClick),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (emotion in item.emotions) {
                        EmotionTag(emotion = emotion)
                    }
                }
                Text(
                    text = item.title,
                    style = MooiTheme.typography.body4,
                    color = MooiTheme.colorScheme.primary,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
private fun ArrivedTimeCapsuleDateLine(
    date: LocalDate,
    modifier: Modifier = Modifier,
    showDate: Boolean = false,
    isFirstDayOfMonth: Boolean = false,
    isLastDayOfMonth: Boolean = false
) {
    // hide date & decorative line
    if (!showDate && isLastDayOfMonth) {
        Box(
            modifier = modifier
                .fillMaxHeight()
                .width(49.dp)
        )
        return
    }

    Column(
        modifier = modifier
            .fillMaxHeight()
            .width(49.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // decorative line start
        if (isFirstDayOfMonth) {
            Column(
                modifier = Modifier
                    .height(33.dp)
                    .padding(top = 12.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(MooiTheme.colorScheme.gray600, CircleShape)
                )
                Box(
                    modifier = Modifier
                        .width(1.5.dp)
                        .height(17.dp)
                        .padding(top = 5.dp)
                        .background(MooiTheme.colorScheme.gray800)
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .width(1.5.dp)
                    .height(33.dp)
                    .background(MooiTheme.colorScheme.gray800)
            )
        }

        // date
        if (showDate) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp)
                    .background(
                        Color(0xFF0E0C12),
                        RoundedCornerShape(10.dp)
                    )
                    .border(
                        1.dp,
                        Color(0xF3C3C3C).copy(alpha = 0.7f),
                        RoundedCornerShape(10.dp)
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier.height(22.dp),
                    text = date.dayOfMonth.toString(),
                    style = MooiTheme.typography.body1.copy(fontWeight = FontWeight.SemiBold),
                    color = Color.White
                )
                Text(
                    modifier = Modifier.height(22.dp),
                    text = date.getKorDayOfWeek().first().toString(),
                    style = MooiTheme.typography.body4.copy(fontSize = 16.sp),
                    color = MooiTheme.colorScheme.gray600
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .width(1.5.dp)
                    .height(72.dp)
                    .background(MooiTheme.colorScheme.gray800)
            )
        }

        // decorative line end
        if (!isLastDayOfMonth) {
            Box(
                modifier = Modifier
                    .width(1.5.dp)
                    .weight(1f)
                    .background(MooiTheme.colorScheme.gray800)
            )
        }
    }
}


@Preview
@Composable
private fun ArrivedScreenPreview() {
    MooiTheme {
        StatelessArrivedScreen(
            arrivedTimeCapsules = DUMMY_ARRIVED
        )
    }
}