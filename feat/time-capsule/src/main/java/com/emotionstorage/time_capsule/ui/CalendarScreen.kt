package com.emotionstorage.time_capsule.ui

import androidx.compose.foundation.Image
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.emotionstorage.time_capsule.presentation.CalendarAction
import com.emotionstorage.time_capsule.presentation.CalendarSideEffect
import com.emotionstorage.time_capsule.presentation.CalendarState
import com.emotionstorage.time_capsule.presentation.CalendarViewModel
import com.emotionstorage.time_capsule.ui.component.TimeCapsuleCalendar
import com.emotionstorage.time_capsule.ui.component.TimeCapsuleCalendarBottomSheet
import com.emotionstorage.time_capsule.ui.model.TimeCapsuleItemState
import com.emotionstorage.ui.R
import com.emotionstorage.ui.component.IconWithCount
import com.emotionstorage.ui.theme.MooiTheme
import com.emotionstorage.ui.util.mainBackground
import com.emotionstorage.ui.util.subBackground
import java.time.LocalDate

// state for calendar screen bottom sheet
data class CalendarBottomSheetState(
    val showBottomSheet: Boolean = false,
    val date: LocalDate? = null,
    val timeCapsules: List<TimeCapsuleItemState> = emptyList(),
    val dailyReportId: String? = null,
    val isNewDailyReport: Boolean = false,
)

@Composable
fun CalendarScreen(
    modifier: Modifier = Modifier,
    viewModel: CalendarViewModel = hiltViewModel(),
    navToKey: () -> Unit = {},
    navToArrived: () -> Unit = {},
    navToFavorites: () -> Unit = {},
    navToTimeCapsuleDetail: (id: String) -> Unit = {},
    navToDailyReportDetail: (id: String) -> Unit = {},
    navToAIChat: (roomId: String) -> Unit = {},
) {
    val state = viewModel.container.stateFlow.collectAsState()
    LifecycleResumeEffect(Unit) {
        viewModel.onAction(CalendarAction.Initiate)
        onPauseOrDispose { }
    }

    var bottomSheetState = remember { mutableStateOf(CalendarBottomSheetState()) }
    LaunchedEffect(Unit) {
        viewModel.container.sideEffectFlow.collect { sideEffect ->
            when (sideEffect) {
                is CalendarSideEffect.ShowBottomSheet -> {
                    // update bottom sheet state
                    bottomSheetState.value =
                        bottomSheetState.value.copy(
                            showBottomSheet = true,
                            date = sideEffect.date,
                            timeCapsules = sideEffect.timeCapsules,
                            dailyReportId = sideEffect.dailyReportId,
                            isNewDailyReport = sideEffect.isNewDailyReport,
                        )
                }

                is CalendarSideEffect.EnterCharRoomSuccess -> {
                    navToAIChat(sideEffect.roomId)
                }
            }
        }
    }


    StatelessCalendarScreen(
        modifier = modifier,
        bottomSheetState = bottomSheetState.value,
        onDismissBottomSheet = {
            bottomSheetState.value = bottomSheetState.value.copy(showBottomSheet = false)
        },
        state = state.value,
        viewModel::onAction,
        navToKey = navToKey,
        navToArrived = navToArrived,
        navToFavorites = navToFavorites,
        navToTimeCapsuleDetail = navToTimeCapsuleDetail,
        navToDailyReportDetail = navToDailyReportDetail,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StatelessCalendarScreen(
    modifier: Modifier = Modifier,
    bottomSheetState: CalendarBottomSheetState = CalendarBottomSheetState(),
    onDismissBottomSheet: () -> Unit = {},
    state: CalendarState = CalendarState(),
    onAction: (CalendarAction) -> Unit = {},
    navToKey: () -> Unit = {},
    navToArrived: () -> Unit = {},
    navToFavorites: () -> Unit = {},
    navToTimeCapsuleDetail: (id: String) -> Unit = {},
    navToDailyReportDetail: (id: String) -> Unit = {},
) {
    Scaffold(
        modifier
            .fillMaxSize()
            .background(MooiTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        topBar = {
            CalendarTopBar(
                modifier = Modifier.fillMaxWidth(),
                calendarMonth = state.calendarYearMonth.monthValue,
                keyCount = state.keyCount,
                navToKey = navToKey,
            )
        },
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(MooiTheme.colorScheme.background)
                    .padding(innerPadding),
        ) {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                CalendarNavButton(
                    modifier = Modifier.weight(1f),
                    label = "도착한 타임캡슐",
                    showNewBadge = true,
                    onClick = navToArrived,
                )

                CalendarNavButton(
                    modifier = Modifier.weight(1f),
                    label = "내 마음 서랍",
                    showNewBadge = false,
                    onClick = navToFavorites,
                )
            }

            TimeCapsuleCalendar(
                modifier = Modifier.fillMaxWidth(),
                calendarYearMonth = state.calendarYearMonth,
                onCalendarYearMonthSelect = {
                    onAction(CalendarAction.SelectCalendarYearMonth(it))
                },
                timeCapsuleDates = state.timeCapsuleDates,
                onDateSelect = {
                    onAction(CalendarAction.SelectCalendarDate(it))
                }
            )

            CalendarTodayActionButton(
                modifier = Modifier.padding(top = 30.dp),
                madeTimeCapsuleToday = state.madeTimeCapsuleToday,
                onTodayAction = {
                    // change calendar year & month to today
                    onAction(CalendarAction.SelectCalendarYearMonth(LocalDate.now().withDayOfMonth(1)))
                    // open today's bottom sheet
                    onAction(CalendarAction.SelectCalendarDate(LocalDate.now()))
                },
                onChatAction = {
                    onAction(CalendarAction.EnterChat)
                }
            )

            // calendar date bottom sheet
            if (bottomSheetState.showBottomSheet && bottomSheetState.date != null) {
                TimeCapsuleCalendarBottomSheet(
                    date = bottomSheetState.date,
                    onDismissRequest = onDismissBottomSheet,
                    timeCapsules = bottomSheetState.timeCapsules,
                    navToTimeCapsuleDetail = navToTimeCapsuleDetail,
                    navToDailyReport = bottomSheetState.dailyReportId?.run {
                        { navToDailyReportDetail(this) }
                    },
                    isNewDailyReport = bottomSheetState.isNewDailyReport,
                )
            }
        }
    }
}

@Composable
private fun CalendarTopBar(
    calendarMonth: Int,
    keyCount: Int,
    modifier: Modifier = Modifier,
    navToKey: () -> Unit = {},
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .height(91.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text =
                buildAnnotatedString {
                    withStyle(
                        SpanStyle(
                            color = MooiTheme.colorScheme.primary,
                        ),
                    ) {
                        append("${calendarMonth}월")
                    }
                    append("의 내 감정")
                },
            style = MooiTheme.typography.head3,
            color = Color.White,
        )

        IconWithCount(
            modifier = Modifier.size(32.dp),
            iconId = R.drawable.key,
            count = keyCount,
            onClick = navToKey,
        )
    }
}

@Composable
private fun CalendarNavButton(
    modifier: Modifier = Modifier,
    label: String,
    onClick: () -> Unit = {},
    showNewBadge: Boolean = false,
) {
    Box(
        modifier =
            modifier
                .height(53.dp)
                .subBackground(true, RoundedCornerShape(10.dp))
                .clickable {
                    onClick()
                },
    ) {
        if (showNewBadge) {
            Box(
                modifier =
                    Modifier
                        .align(Alignment.TopEnd)
                        .size(20.dp)
                        .offset(x = 7.dp, y = -7.dp)
                        .background(MooiTheme.colorScheme.secondary, CircleShape),
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "N",
                    style = MooiTheme.typography.button.copy(fontSize = 10.sp),
                    color = Color.White,
                )
            }
        }
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = label,
            style = MooiTheme.typography.body4,
            color = Color.White,
        )
    }
}

@Composable
private fun CalendarTodayActionButton(
    modifier: Modifier = Modifier,
    madeTimeCapsuleToday: Boolean = false,
    onTodayAction: () -> Unit = {},
    onChatAction: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent)
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.Center)
                .mainBackground(true, RoundedCornerShape(500.dp))
                .clickable {
                    if (madeTimeCapsuleToday) onTodayAction() else onChatAction()
                }
                .height(44.dp)
                .padding(horizontal = 25.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = if (madeTimeCapsuleToday) "오늘 내 감정 보기" else "오늘 감정 기록하러가기",
                style = MooiTheme.typography.body3.copy(lineHeight = 24.sp, color = Color.White),
            )
            Image(
                modifier = Modifier.size(8.dp, 14.dp).rotate(180f),
                painter = painterResource(R.drawable.arrow_back),
                contentDescription = null,
            )
        }
    }
}

@Preview
@Composable
private fun CalendarScreenPreview() {
    MooiTheme {
        StatelessCalendarScreen()
    }
}
