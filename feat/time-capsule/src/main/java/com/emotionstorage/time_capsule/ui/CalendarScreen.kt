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
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.map
import com.emotionstorage.time_capsule.presentation.CalendarAction
import com.emotionstorage.time_capsule.presentation.CalendarSideEffect
import com.emotionstorage.time_capsule.presentation.CalendarState
import com.emotionstorage.time_capsule.presentation.CalendarViewModel
import com.emotionstorage.time_capsule.presentation.ToggleFavoriteAction
import com.emotionstorage.time_capsule.presentation.ToggleFavoriteSideEffect.ShowFavoriteFailToast
import com.emotionstorage.time_capsule.presentation.ToggleFavoriteSideEffect.ShowFavoriteSuccessToast
import com.emotionstorage.time_capsule.presentation.ToggleFavoriteState
import com.emotionstorage.time_capsule.presentation.ToggleFavoriteViewModel
import com.emotionstorage.ui.component.bottomSheet.YearMonthPickerBottomSheet
import com.emotionstorage.time_capsule.ui.component.TimeCapsuleCalendar
import com.emotionstorage.time_capsule.ui.component.TimeCapsuleBottomSheet
import com.emotionstorage.ui.R
import com.emotionstorage.ui.component.toast.AppSnackbarHost
import com.emotionstorage.ui.component.IconWithCount
import com.emotionstorage.ui.component.toast.AppSnackbarController
import com.emotionstorage.ui.theme.MooiTheme
import com.emotionstorage.ui.util.mainBackground
import com.emotionstorage.ui.util.subBackground
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun CalendarScreen(
    modifier: Modifier = Modifier,
    viewModel: CalendarViewModel = hiltViewModel(),
    favoriteViewModel: ToggleFavoriteViewModel = hiltViewModel(),
    bottomAppBar: @Composable () -> Unit = {},
    navToKey: () -> Unit = {},
    navToArrived: () -> Unit = {},
    navToFavorites: () -> Unit = {},
    navToTimeCapsuleDetail: (id: Long) -> Unit = {},
    navToDailyReportDetail: (id: Long) -> Unit = { },
    navToAIChat: (roomId: Long) -> Unit = {},
) {
    val context = LocalContext.current

    val state = viewModel.container.stateFlow.collectAsState()
    val timeCapsulesState = state.value.timeCapsulesFlow?.collectAsLazyPagingItems()
    val favoriteState = favoriteViewModel.container.stateFlow.collectAsState()

    // init screen on resume
    LifecycleResumeEffect(Unit) {
        viewModel.onAction(CalendarAction.Initiate)
        onPauseOrDispose { }
    }

    // update favorite state
    LaunchedEffect(timeCapsulesState?.itemSnapshotList?.items) {
        val favorites =
            timeCapsulesState
                ?.itemSnapshotList
                ?.items
                ?.filter { it.isFavorite }
                ?.map { it.id }
                ?: emptyList()

        favoriteViewModel.onAction(ToggleFavoriteAction.Init(favorites))
    }

    val snackState = remember { SnackbarHostState() }
    val snackbarController = remember { AppSnackbarController(snackState) }
    val (showYearMonthBottomSheet, setShowYearMonthBottomSheet) = remember { mutableStateOf(false) }
    val (showTimeCapsuleBottomSheet, setShowTimeCapsuleBottomSheet) = remember { mutableStateOf(false) }

    // collect side effect
    LaunchedEffect(Unit) {
        viewModel.container.sideEffectFlow.collect { sideEffect ->
            when (sideEffect) {
                is CalendarSideEffect.ShowTimeCapsuleBottomSheet -> {
                    setShowTimeCapsuleBottomSheet(true)
                }

                is CalendarSideEffect.EnterCharRoomSuccess -> {
                    navToAIChat(sideEffect.roomId)
                }
            }
        }
    }

    // collect favorite side effect
    LaunchedEffect("init") {
        favoriteViewModel.container.sideEffectFlow.collect {
            when (it) {
                is ShowFavoriteSuccessToast -> {
                    snackState.currentSnackbarData?.dismiss()
                    snackbarController.showSnackbar(
                        message =
                            if (it.isFavorite) {
                                context.getString(R.string.toast_favorite_added)
                            } else {
                                context.getString(R.string.toast_favorite_removed)
                            },
                        iconResId = R.drawable.ic_success_filled,
                    )
                }

                is ShowFavoriteFailToast -> {
                    snackState.currentSnackbarData?.dismiss()
                    snackbarController.showSnackbar(
                        message = context.getString(R.string.toast_favorite_full),
                    )
                }
            }
        }
    }

    StatelessCalendarScreen(
        modifier = modifier,
        bottomAppBar = bottomAppBar,
        snackState = snackState,
        snackbarController = snackbarController,
        showYearMonthBottomSheet = showYearMonthBottomSheet,
        setShowYearMonthBottomSheet = setShowYearMonthBottomSheet,
        showTimeCapsuleBottomSheet = showTimeCapsuleBottomSheet,
        setShowTimeCapsuleBottomSheet = setShowTimeCapsuleBottomSheet,
        state = state.value,
        favoriteState = favoriteState.value,
        onAction = viewModel::onAction,
        onFavoriteAction = favoriteViewModel::onAction,
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
    bottomAppBar: @Composable () -> Unit = {},
    snackState: SnackbarHostState = SnackbarHostState(),
    snackbarController: AppSnackbarController = remember { AppSnackbarController(SnackbarHostState()) },
    showYearMonthBottomSheet: Boolean = false,
    setShowYearMonthBottomSheet: (Boolean) -> Unit = {},
    showTimeCapsuleBottomSheet: Boolean = false,
    setShowTimeCapsuleBottomSheet: (Boolean) -> Unit = {},
    state: CalendarState = CalendarState(),
    favoriteState: ToggleFavoriteState = ToggleFavoriteState(),
    onAction: (CalendarAction) -> Unit = {},
    onFavoriteAction: (ToggleFavoriteAction) -> Unit = {},
    navToKey: () -> Unit = {},
    navToArrived: () -> Unit = {},
    navToFavorites: () -> Unit = {},
    navToTimeCapsuleDetail: (id: Long) -> Unit = {},
    navToDailyReportDetail: (id: Long) -> Unit = { },
) {
    Scaffold(
        modifier =
            modifier
                .fillMaxSize()
                .background(MooiTheme.colorScheme.background),
        snackbarHost = {
            AppSnackbarHost(
                hostState = snackState,
                customDataFlow = snackbarController.currentData,
            )
        },
        bottomBar = bottomAppBar,
    ) { innerPadding ->
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(MooiTheme.colorScheme.background)
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(
                    modifier =
                        modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
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
                                    append("${state.calendarYearMonth.monthValue}월")
                                }
                                append("의 내 감정")
                            },
                        style = MooiTheme.typography.head3,
                        color = Color.White,
                    )

                    IconWithCount(
                        modifier = Modifier.size(32.dp),
                        iconId = R.drawable.ic_key,
                        count = state.keyCount,
                        onClick = navToKey,
                    )
                }

                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    CalendarNavButton(
                        modifier = Modifier.weight(1f),
                        label = "도착한 타임캡슐",
                        // todo: add new arrived timecapsules logic
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
                    onDropDownIconClick = {
                        setShowYearMonthBottomSheet(true)
                    },
                    timeCapsuleDates = state.calendarTimeCapsuleDates,
                    onDateSelect = {
                        if (it in state.calendarTimeCapsuleDates) {
                            onAction(CalendarAction.SelectCalendarDate(it))
                        }
                    },
                )
            }

            CalendarTodayActionButton(
                modifier =
                    Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 24.dp),
                madeTimeCapsuleToday = state.madeTimeCapsuleToday,
                onTodayAction = {
                    // change calendar year & month to today
                    onAction(CalendarAction.SelectCalendarYearMonth(YearMonth.now()))
                    // open today's bottom sheet
                    onAction(CalendarAction.SelectCalendarDate(LocalDate.now()))
                },
                onChatAction = {
                    onAction(CalendarAction.EnterChat)
                },
            )

            // calendar year month bottom sheet
            if (showYearMonthBottomSheet) {
                YearMonthPickerBottomSheet(
                    onDismissRequest = {
                        setShowYearMonthBottomSheet(false)
                    },
                    selectedYearMonth = state.calendarYearMonth,
                    onYearMonthSelect = {
                        setShowYearMonthBottomSheet(false)
                        onAction(CalendarAction.SelectCalendarYearMonth(it))
                    },
                )
            }

            // calendar date's time capsule bottom sheet
            if (showTimeCapsuleBottomSheet && state.calendarSelectedDate != null && state.timeCapsulesFlow != null) {
                TimeCapsuleBottomSheet(
                    date = state.calendarSelectedDate,
                    onDismissRequest = {
                        setShowTimeCapsuleBottomSheet(false)
                        onAction(CalendarAction.ClearBottomSheet)
                    },
                    timeCapsulesFlow =
                        state.timeCapsulesFlow.map {
                            it.map {
                                it.copy(
                                    isFavorite = favoriteState.favoriteIds.contains(it.id),
                                )
                            }
                        },
                    onToggleFavorite = { id, prevFavorite ->
                        onFavoriteAction(ToggleFavoriteAction.OnToggle(id))
                    },
                    navToTimeCapsuleDetail = {
                        setShowTimeCapsuleBottomSheet(false)
                        navToTimeCapsuleDetail(it)
                    },
                    navToDailyReport =
                        state.dailyReportId?.run {
                            {
                                setShowTimeCapsuleBottomSheet(false)
                                navToDailyReportDetail(this)
                            }
                        },
                    isNewDailyReport = state.isNewDailyReport,
                )
            }
        }
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
                    style = MooiTheme.typography.mainButton.copy(fontSize = 10.sp),
                    color = Color.White,
                )
            }
        }
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = label,
            style = MooiTheme.typography.caption3,
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
        modifier =
            modifier
                .fillMaxWidth()
                .background(Color.Transparent),
    ) {
        Row(
            modifier =
                Modifier
                    .align(Alignment.Center)
                    .mainBackground(true, RoundedCornerShape(500.dp))
                    .clickable {
                        if (madeTimeCapsuleToday) onTodayAction() else onChatAction()
                    }.height(44.dp)
                    .padding(horizontal = 25.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                text = if (madeTimeCapsuleToday) "오늘 내 감정 보기" else "오늘 감정 기록하러가기",
                style = MooiTheme.typography.body6.copy(color = Color.White),
            )
            Image(
                modifier =
                    Modifier
                        .size(8.dp, 14.dp)
                        .rotate(180f),
                painter = painterResource(R.drawable.ic_arrow_back),
                contentDescription = null,
            )
        }
    }
}

@PreviewScreenSizes
@Composable
private fun CalendarScreenPreview() {
    MooiTheme {
        StatelessCalendarScreen()
    }
}
