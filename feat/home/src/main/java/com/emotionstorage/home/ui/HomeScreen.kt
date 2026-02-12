package com.emotionstorage.home.ui

import android.view.Gravity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.emotionstorage.domain.model.ChatEntry
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
import com.emotionstorage.ui.annotation.PreviewScreenRatios
import com.emotionstorage.ui.component.IconWithCount
import com.emotionstorage.ui.component.button.CtaButton
import com.emotionstorage.ui.component.loading.LoadingOverlay
import com.emotionstorage.ui.component.modal.LoginSessionExpiredModal
import com.emotionstorage.ui.component.modal.TempErrorModal
import com.emotionstorage.ui.component.toast.AppSnackbarHost
import com.emotionstorage.ui.component.toast.Toast
import com.emotionstorage.ui.theme.MooiTheme
import kotlinx.coroutines.launch

private sealed class ModalState {
    object None : ModalState()

    object ResumeChat : ModalState()

    object LoginSessionExpired : ModalState()

    object TempError : ModalState()
}

@Composable
fun HomeScreen(
    navToKey: () -> Unit,
    navToAlarm: () -> Unit,
    navToDailyReport: (Long) -> Unit,
    navToChat: (Long, ChatEntry) -> Unit,
    navToArrivedTimeCapsules: () -> Unit,
    navToLogin: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    attendanceViewModel: AttendanceViewModel = hiltViewModel(),
    bottomAppBar: @Composable (() -> Unit) = {},
) {
    val context = LocalContext.current

    val state = viewModel.container.stateFlow.collectAsState()
    val attendanceState = attendanceViewModel.uiState.collectAsState()
    val snackbarState = remember { SnackbarHostState() }
    val (snackbarGravity, setSnackbarGravity) = remember { mutableIntStateOf(Gravity.TOP) }
    val (modalState, setModalState) = remember { mutableStateOf<ModalState>(ModalState.None) }

    val summary = attendanceState.value.summary

//    NotificationPermissionAutoRequest()

    LaunchedEffect("init") {
        // load attendance state
        attendanceViewModel.onAction(AttendanceAction.Init)

        // collect side effect
        viewModel.container.sideEffectFlow.collect { sideEffect ->
            when (sideEffect) {
                is HomeSideEffect.TicketNotEnough -> {
                    setSnackbarGravity(Gravity.TOP)
                    snackbarState.showSnackbar("감정 대화 티켓이 부족해요 😢")
                }

                is HomeSideEffect.EnterChatRoom -> {
                    state.value.roomId?.let {
                        navToChat(it, sideEffect.entry)
                    }
                }

                is BaseSideEffect.NetworkError -> {
                    setSnackbarGravity(Gravity.TOP)
                    snackbarState.showSnackbar(context.getString(R.string.toast_network_error))
                }

                is BaseSideEffect.SessionExpired -> {
                    setModalState(ModalState.LoginSessionExpired)
                }

                is BaseSideEffect.TemporalError -> {
                    setModalState(ModalState.TempError)
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
        snackbarGravity = snackbarGravity,
        setSnackbarGravity = setSnackbarGravity,
        snackbarState = snackbarState,
        setModalState = setModalState,
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

    when (modalState) {
        is ModalState.None -> {
            // no modal
        }

        is ModalState.ResumeChat -> {
            ResumeChatModal(
                onDismissRequest = {
                    setModalState(ModalState.None)
                },
                onResume = {
                    viewModel.onAction(HomeAction.EnterChat)
                },
                onDeleteChat = {
                    viewModel.onAction(HomeAction.DeleteChat)
                },
            )
        }

        is ModalState.LoginSessionExpired -> {
            LoginSessionExpiredModal(
                onDismissRequest = {
                    setModalState(ModalState.None)
                },
                navToLogin = navToLogin,
            )
        }

        is ModalState.TempError -> {
            TempErrorModal(
                onDismissRequest = {
                    setModalState(ModalState.None)
                },
            )
        }
    }
}

@Composable
private fun StatelessHomeScreen(
    modifier: Modifier = Modifier,
    bottomAppBar: @Composable () -> Unit = {},
    state: HomeState = HomeState(),
    snackbarGravity: Int = Gravity.TOP,
    setSnackbarGravity: (Int) -> Unit = {},
    snackbarState: SnackbarHostState = SnackbarHostState(),
    setModalState: (ModalState) -> Unit = {},
    onAction: (HomeAction) -> Unit = {},
    navToKey: () -> Unit = {},
    navToAlarm: () -> Unit = {},
    navToDailyReport: (id: Long) -> Unit = { },
    navToArrivedTimeCapsules: () -> Unit = {},
) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier =
            modifier
                .fillMaxSize()
                .background(MooiTheme.colorScheme.backgroundDefault),
        bottomBar = bottomAppBar,
        snackbarHost = {
            AppSnackbarHost(hostState = snackbarState, gravity = snackbarGravity) { message, iconId ->
                Toast(
                    message = message,
                    iconId = iconId,
                    // padding to prevent bottom overlapping
                    outerPaddingValues = PaddingValues(bottom = 100.dp),
                )
            }
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

                StartChatButton(
                    ticketCount = state.ticketCount,
                    isChatTempSaved = state.isChatTempSaved,
                    onChatStart = {
                        onAction(HomeAction.EnterChat)
                    },
                    onChatResume = {
                        setModalState(ModalState.ResumeChat)
                    },
                    onTicketInfoClick = {
                        setSnackbarGravity(Gravity.BOTTOM)
                        coroutineScope.launch {
                            snackbarState.showSnackbar(
                                "하루에 최대 10번까지 감정대화를 나눌 수 있어요.\n" +
                                    "자정 이후에는 횟수가 다시 충전돼요.",
                            )
                        }
                    },
                )
            }
        }
    }
}

@Composable
private fun StartChatButton(
    ticketCount: Int,
    isChatTempSaved: Boolean,
    onChatResume: () -> Unit,
    onChatStart: () -> Unit,
    onTicketInfoClick: () -> Unit,
    modifier: Modifier = Modifier,
    ticketLimit: Int = 10,
) {
    val canStartChat = isChatTempSaved || (ticketCount > 0)
    val isOnGoingLastChat = isChatTempSaved && ticketCount == 0

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        // chat start/resume button
        CtaButton(
            modifier =
                modifier
                    .width(
                        if (canStartChat) 198.dp else 197.dp,
                    ).height(
                        if (canStartChat) 54.dp else 65.dp,
                    ),
            enabled = canStartChat,
            onClick = {
                if (isChatTempSaved) {
                    onChatResume()
                } else {
                    onChatStart()
                }
            },
            radius = 10,
            isDefaultHeight = false,
            isDefaultWidth = false,
        ) {
            if (canStartChat) {
                if (isChatTempSaved) {
                    Text(
                        modifier = Modifier.padding(end = 7.dp),
                        text = "대화 이어하기",
                        style = MooiTheme.typography.mainButton,
                    )
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            modifier = Modifier.padding(end = 7.dp),
                            text = "대화 시작하기",
                            style = MooiTheme.typography.mainButton,
                        )
                        Icon(
                            modifier = Modifier.size(18.dp),
                            painter = painterResource(id = R.drawable.ic_ticket),
                            contentDescription = "ticket",
                            tint = Color.White.copy(alpha = 0.7f),
                        )
                        Text(
                            text = "-1",
                            style = MooiTheme.typography.mainButton,
                            color = Color.White.copy(alpha = 0.7f),
                        )
                    }
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

        // ticket info
        if (isOnGoingLastChat) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp),
            ) {
                Icon(
                    modifier = Modifier.size(16.dp),
                    painter = painterResource(id = R.drawable.ic_caution),
                    contentDescription = null,
                    tint = MooiTheme.colorScheme.secondaryBlue700,
                )
                Text(
                    text = "마지막 대화가 진행 중이에요.",
                    style = MooiTheme.typography.body7,
                    color = MooiTheme.colorScheme.secondaryBlue700,
                )
            }
        } else {
            Row(
                modifier =
                    Modifier.clickable(
                        onClick = onTicketInfoClick,
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp),
            ) {
                Icon(
                    modifier = Modifier.size(18.dp),
                    painter = painterResource(id = R.drawable.ic_ticket),
                    contentDescription = "ticket",
                    tint = MooiTheme.colorScheme.secondaryBlue700,
                )
                Text(
                    text = "감정 대화 티켓",
                    style = MooiTheme.typography.body7,
                    color = MooiTheme.colorScheme.secondaryBlue700,
                )
                Text(
                    text = "$ticketCount/$ticketLimit",
                    style = MooiTheme.typography.body7,
                    color = MooiTheme.colorScheme.secondaryBlue700,
                )
                Icon(
                    modifier =
                        Modifier
                            .size(12.dp)
                            .offset(y = (-7).dp),
                    painter = painterResource(id = R.drawable.ic_question),
                    tint = MooiTheme.colorScheme.gray600,
                    contentDescription = "ticket info",
                )
            }
        }
    }
}

class HomeStateProvider(
    sampleState: HomeState =
        HomeState(
            nickname = "찡찡이",
            roomId = 1,
            keyCount = 10,
            ticketCount = 10,
            newNotificationArrived = true,
            newTimeCapsuleArrived = true,
            newReportArrived = true,
        ),
) : PreviewParameterProvider<HomeState> {
    override val values =
        sequenceOf<HomeState>(
            sampleState,
            sampleState.copy(
                isChatTempSaved = true,
            ),
            sampleState.copy(
                isChatTempSaved = true,
                ticketCount = 0,
            ),
            sampleState.copy(
                ticketCount = 0,
            ),
        )
}

@Preview
@Composable
private fun HomeScreenPreview(
    @PreviewParameter(HomeStateProvider::class) state: HomeState,
) {
    MooiTheme {
        StatelessHomeScreen(
            state = state,
        )
    }
}

@PreviewScreenRatios
@Composable
private fun HomeScreenPreview2() {
    MooiTheme {
        StatelessHomeScreen(
            state =
                HomeState(
                    nickname = "찡찡이",
                    roomId = 1,
                    keyCount = 10,
                    ticketCount = 10,
                    newNotificationArrived = true,
                    newTimeCapsuleArrived = true,
                    newReportArrived = true,
                ),
        )
    }
}
