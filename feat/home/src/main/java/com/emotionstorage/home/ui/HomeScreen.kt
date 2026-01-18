package com.emotionstorage.home.ui

import android.view.Gravity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.emotionstorage.home.presentation.AttendanceAction
import com.emotionstorage.home.presentation.AttendanceViewModel
import com.emotionstorage.home.presentation.HomeAction
import com.emotionstorage.home.presentation.HomeSideEffect
import com.emotionstorage.home.presentation.HomeState
import com.emotionstorage.home.presentation.HomeViewModel
import com.emotionstorage.home.ui.component.AttendanceRewardDialog
import com.emotionstorage.home.ui.modal.AttendanceRefreshModal
import com.emotionstorage.home.ui.modal.ResumeChatModal
import com.emotionstorage.presentation.BaseSideEffect
import com.emotionstorage.ui.R
import com.emotionstorage.ui.component.IconWithCount
import com.emotionstorage.ui.component.button.CtaButton
import com.emotionstorage.ui.component.loading.LoadingOverlay
import com.emotionstorage.ui.component.toast.AppSnackbarHost
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    attendanceViewModel: AttendanceViewModel = hiltViewModel(),
    bottomAppBar: @Composable (() -> Unit) = {},
    navToKey: () -> Unit = {},
    navToAlarm: () -> Unit = {},
    navToDailyReport: (Long) -> Unit = { },
    navToChat: (Long) -> Unit = {},
    navToArrivedTimeCapsules: () -> Unit = {},
) {
    val context = LocalContext.current

    val state = viewModel.container.stateFlow.collectAsState()
    val attendanceState = attendanceViewModel.uiState.collectAsState()
    val snackbarState = remember { SnackbarHostState() }
    val summary = attendanceState.value.summary

//    NotificationPermissionAutoRequest()

    LaunchedEffect("init") {
        // load attendance state
        attendanceViewModel.onAction(AttendanceAction.Init)

        // collect side effect
        viewModel.container.sideEffectFlow.collect {
            when (it) {
                is HomeSideEffect.TicketNotEnough -> {
                    // todo: 티켓 개수 부족한 경우 에러 처리
                    snackbarState.showSnackbar("감정 대화 티켓이 부족해요 😢")
                }

                is HomeSideEffect.EnterChatRoom -> {
                    navToChat(it.roomId)
                }

                is BaseSideEffect.NetworkError -> {
                    snackbarState.showSnackbar(context.getString(R.string.toast_network_error))
                }

                is BaseSideEffect.SessionExpired -> {
                    // todo: show session expired modal
                }

                is BaseSideEffect.TemporalError -> {
                    // todo: show temporal error modal
                }
            }
        }
    }

    LifecycleResumeEffect("onResume") {
        // init screen state on resume
        viewModel.onAction(HomeAction.Initiate)
        onPauseOrDispose {}
    }

    StatelessHomeScreen(
        modifier = modifier,
        bottomAppBar = bottomAppBar,
        state = state.value,
        snackbarState = snackbarState,
        onAction = viewModel::onAction,
        navToKey = navToKey,
        navToAlarm = navToAlarm,
        navToDailyReport = navToDailyReport,
        navToArrivedTimeCapsules = navToArrivedTimeCapsules,
    )

    if (attendanceState.value.showDialog && summary != null) {
        AttendanceRewardDialog(
            summary = summary,
            onConfirm = { attendanceViewModel.onAction(AttendanceAction.ConfirmReward) },
        )
    }

    if (attendanceState.value.showDayChangedAlert) {
        AttendanceRefreshModal(
            onConfirm = { attendanceViewModel.onAction(AttendanceAction.ConfirmDayChanged) },
        )
    }

    ResumeChatModal(
        isModalOpen = state.value.showResumeChatModal && state.value.pendingChatRoomId != null,
        onDismissRequest = {
            viewModel.onAction(HomeAction.DismissResumeChat)
        },
        onResume = {
            viewModel.onAction(HomeAction.ConfirmResumeChat)
        },
        onDropAndStartNew = {
            viewModel.onAction(HomeAction.DismissResumeChat)
        },
    )
}

@Composable
private fun StatelessHomeScreen(
    modifier: Modifier = Modifier,
    bottomAppBar: @Composable () -> Unit = {},
    state: HomeState = HomeState(),
    snackbarState: SnackbarHostState = SnackbarHostState(),
    onAction: (HomeAction) -> Unit = {},
    navToKey: () -> Unit = {},
    navToAlarm: () -> Unit = {},
    navToDailyReport: (id: Long) -> Unit = { },
    navToArrivedTimeCapsules: () -> Unit = {},
) {
    Scaffold(
        modifier =
            modifier
                .fillMaxSize()
                .background(MooiTheme.colorScheme.backgroundDefault),
        bottomBar = bottomAppBar,
        snackbarHost = {
            AppSnackbarHost(hostState = snackbarState, gravity = Gravity.TOP)
        },
    ) { innerPadding ->
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(MooiTheme.colorScheme.backgroundDefault)
                    .padding(innerPadding),
        ) {
            // bg & character graphic
            Image(
                modifier =
                    Modifier
                        .zIndex(-10f)
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth(),
                painter = painterResource(id = R.drawable.graphic_home_bg),
                contentDescription = null,
            )
            Image(
                modifier =
                    Modifier
                        .zIndex(-9f)
                        .align(Alignment.BottomCenter)
                        .size(245.dp, 198.dp)
                        .offset(y = (-36).dp),
                painter = painterResource(id = R.drawable.graphic_home_mooi),
                contentDescription = null,
            )

            // loading overlay
            if (state.isLoading) {
                LoadingOverlay()
            }

            // icons
            Column(
                modifier =
                    Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 14.dp)
                        .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(15.dp),
                horizontalAlignment = Alignment.End,
            ) {
                Row(
                    modifier = Modifier,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(15.dp),
                ) {
                    IconWithCount(
                        modifier = Modifier.size(30.dp),
                        iconId = R.drawable.ic_key,
                        count = state.keyCount,
                        onClick = navToKey,
                    )
                    Image(
                        modifier =
                            Modifier
                                .size(30.dp)
                                .clickable {
                                    navToAlarm()
                                },
                        painter =
                            painterResource(
                                id = if (state.newNotificationArrived) R.drawable.ic_alarm_new else R.drawable.ic_alarm,
                            ),
                        contentDescription = "alarm",
                    )
                }
                if (state.newTimeCapsuleArrived) {
                    Image(
                        modifier =
                            Modifier
                                .size(30.dp)
                                .clickable {
                                    navToArrivedTimeCapsules()
                                },
                        painter = painterResource(id = R.drawable.ic_time_capsule_new),
                        contentDescription = "new time capsule arrived",
                    )
                }
                if (state.newReportArrived) {
                    state.newReportId?.run {
                        Image(
                            modifier =
                                Modifier
                                    .size(30.dp)
                                    .clickable {
                                        navToDailyReport(this)
                                    },
                            painter = painterResource(id = R.drawable.ic_daily_report_new),
                            contentDescription = "new daily report arrived",
                        )
                    }
                }
            }

            // home content
            Column(
                modifier =
                    Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 179.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text =
                        buildAnnotatedString {
                            append("${state.nickname}님,\n")
                            withStyle(SpanStyle(color = MooiTheme.colorScheme.primaryBlue500)) {
                                append("오늘의 기분")
                            }
                            append("은 어떤가요?")
                        },
                    style = MooiTheme.typography.brandFont1,
                    textAlign = TextAlign.Center,
                    color = Color.White,
                )
                Spacer(modifier = Modifier.height(9.dp))
                Text(
                    modifier = Modifier.padding(top = 2.dp),
                    text = "대화로 내 감정을 들여다보고\n타임캡슐로 저장해보세요",
                    style =
                        MooiTheme.typography.body8.copy(
                            fontWeight = FontWeight.Light,
                            lineHeight = 22.sp,
                        ),
                    textAlign = TextAlign.Center,
                    color = MooiTheme.colorScheme.gray500,
                )
                Spacer(modifier = Modifier.height(20.dp))
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    StartChatButton(
                        canStartChat = state.ticketCount > 0,
                        onChatStart = {
                            onAction(HomeAction.EnterChat)
                        },
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                    ) {
                        Image(
                            modifier = Modifier.size(18.dp),
                            painter = painterResource(id = R.drawable.ic_ticket),
                            contentDescription = "ticket",
                            colorFilter = ColorFilter.tint(MooiTheme.colorScheme.secondaryBlue700),
                        )
                        Text(
                            text = "감정 대화 티켓",
                            style = MooiTheme.typography.body7,
                            color = MooiTheme.colorScheme.secondaryBlue700,
                        )
                        Text(
                            text = "${state.ticketCount}/${state.ticketLimit}",
                            style = MooiTheme.typography.body7,
                            color = MooiTheme.colorScheme.secondaryBlue700,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StartChatButton(
    modifier: Modifier = Modifier,
    canStartChat: Boolean = true,
    onChatStart: () -> Unit = {},
) {
    CtaButton(
        modifier =
            modifier
                .width(
                    if (canStartChat) 198.dp else 197.dp,
                ).height(
                    if (canStartChat) 54.dp else 65.dp,
                ),
        enabled = canStartChat,
        onClick = onChatStart,
        radius = 10,
        isDefaultHeight = false,
        isDefaultWidth = false,
    ) {
        if (canStartChat) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier.padding(end = 7.dp),
                    text = "대화 시작하기",
                    style = MooiTheme.typography.mainButton,
                )
                Image(
                    modifier = Modifier.size(18.dp),
                    painter = painterResource(id = R.drawable.ic_ticket),
                    contentDescription = "ticket",
                    colorFilter = ColorFilter.tint(Color.White.copy(alpha = 0.7f)),
                )
                Text(
                    text = "-1",
                    style = MooiTheme.typography.mainButton,
                    color = Color.White.copy(alpha = 0.7f),
                )
            }
        } else {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "대화 시작하기",
                    style = MooiTheme.typography.mainButton,
                )
                Text(
                    text = "(대화 티켓 부족)",
                    style = MooiTheme.typography.body4.copy(lineHeight = 20.sp),
                )
            }
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    MooiTheme {
        StatelessHomeScreen(
            state =
                HomeState(
                    nickname = "찡찡이",
                    keyCount = 3,
                    ticketCount = 5,
                    newNotificationArrived = true,
                    newTimeCapsuleArrived = true,
                    newReportArrived = true,
                ),
        )
    }
}

@Preview
@Composable
private fun HomeScreenPreview2() {
    MooiTheme {
        StatelessHomeScreen(
            state =
                HomeState(
                    nickname = "찡찡이",
                    keyCount = 3,
                    ticketCount = 5,
                    newNotificationArrived = true,
                    newTimeCapsuleArrived = false,
                    newReportArrived = false,
                ),
        )
    }
}
