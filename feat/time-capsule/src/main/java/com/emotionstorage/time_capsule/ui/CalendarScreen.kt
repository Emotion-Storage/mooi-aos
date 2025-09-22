package com.emotionstorage.time_capsule.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emotionstorage.time_capsule.ui.component.TimeCapsuleCalendar
import com.emotionstorage.ui.theme.MooiTheme
import com.emotionstorage.ui.util.subBackground

@Composable
fun CalendarScreen(
    modifier: Modifier = Modifier,
    navToArrived: () -> Unit = {},
    navToFavorites: () -> Unit = {}
) {
    StatelessCalendarScreen(
        modifier,
        navToArrived,
        navToFavorites
    )
}

@Composable
private fun StatelessCalendarScreen(
    modifier: Modifier = Modifier,
    navToArrived: () -> Unit = {},
    navToFavorites: () -> Unit = {}
) {
    Scaffold(
        modifier
            .fillMaxSize()
            .background(MooiTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        topBar = {
            CalendarTopBar(
                calendarMonth = 7,
                keyCount = 1
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MooiTheme.colorScheme.background)
                .padding(innerPadding)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                CalendarNavButton(
                    modifier = Modifier.weight(1f),
                    label = "도착한 타임캡슐",
                    showNewBadge = true,
                    onClick = navToArrived
                )

                CalendarNavButton(
                    modifier = Modifier.weight(1f),
                    label = "내 마음 서랍",
                    showNewBadge = false,
                    onClick = navToFavorites
                )
            }

            TimeCapsuleCalendar(
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun CalendarTopBar(
    calendarMonth: Int,
    keyCount: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(91.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = buildAnnotatedString {
                withStyle(
                    SpanStyle(
                        color = MooiTheme.colorScheme.primary,
                    )
                ) {
                    append("${calendarMonth}월")
                }
                append("의 내 감정")
            },
            style = MooiTheme.typography.head3,
            color = Color.White
        )

        // todo: add key icon
    }
}

@Composable
private fun CalendarNavButton(
    modifier: Modifier = Modifier,
    label: String,
    onClick: () -> Unit = {},
    showNewBadge: Boolean = false
) {
    Box(
        modifier = modifier
            .height(53.dp)
            .subBackground(true, RoundedCornerShape(10.dp))
            .clickable {
                onClick()
            }
    ) {
        if (showNewBadge) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(20.dp)
                    .offset(x = 7.dp, y = -7.dp)
                    .background(MooiTheme.colorScheme.secondary, CircleShape)
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "N",
                    style = MooiTheme.typography.button.copy(fontSize = 10.sp),
                    color = Color.White
                )
            }
        }
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = label,
            style = MooiTheme.typography.body4,
            color = Color.White
        )
    }
}

@Preview
@Composable
private fun CalendarScreenPreview() {
    MooiTheme {
        StatelessCalendarScreen()
    }
}