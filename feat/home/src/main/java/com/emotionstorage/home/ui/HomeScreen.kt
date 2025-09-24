package com.emotionstorage.home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.emotionstorage.home.presentation.HomeAction
import com.emotionstorage.home.presentation.HomeSideEffect
import com.emotionstorage.home.presentation.HomeState
import com.emotionstorage.home.presentation.HomeViewModel
import com.emotionstorage.ui.R
import com.emotionstorage.ui.component.IconWithCount
import com.emotionstorage.ui.component.IconWithCountType
import com.emotionstorage.ui.theme.MooiTheme
import com.emotionstorage.ui.theme.pretendard
import com.emotionstorage.ui.util.mainBackground
import com.orhanobut.logger.Logger

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    navToChat: (roomId: String) -> Unit = {},
    navToArrivedTimeCapsules: () -> Unit = {},
) {
    val state = viewModel.container.stateFlow.collectAsState()

    LaunchedEffect(Unit) {
        Logger.d("HomeScreen: Launch triggered")
        // init nickname on launch
        viewModel.onAction(HomeAction.InitNickname)
    }

    LifecycleResumeEffect(Unit) {
        Logger.d("HomeScreen: onResume triggered")
        // update screen state on resume
        viewModel.onAction(HomeAction.Initiate)
        onPauseOrDispose {
            // do nothing
        }
    }

    LaunchedEffect(Unit) {
        viewModel.container.sideEffectFlow.collect {
            when (it) {
                is HomeSideEffect.EnterCharRoomSuccess -> {
                    navToChat(it.roomId)
                }
            }
        }
    }

    StatelessHomeScreen(
        modifier = modifier,
        state = state.value,
        onAction = viewModel::onAction,
        navToArrivedTimeCapsules = navToArrivedTimeCapsules
    )
}

@Composable
private fun StatelessHomeScreen(
    modifier: Modifier = Modifier,
    state: HomeState = HomeState(),
    onAction: (HomeAction) -> Unit = {},
    navToArrivedTimeCapsules: () -> Unit = {}
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(MooiTheme.colorScheme.background)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MooiTheme.colorScheme.background)
                .padding(horizontal = 16.dp)
                .padding(innerPadding)
        ) {
            // icons
            Column(
                modifier = Modifier.align(Alignment.TopEnd),
                verticalArrangement = Arrangement.spacedBy(15.dp),
                horizontalAlignment = Alignment.End
            ) {
                Row(
                    modifier = Modifier,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    // todo: navigate to key detail screen
                    IconWithCount(
                        modifier = Modifier.size(30.dp),
                        type = IconWithCountType.KEY,
                        count = if(state.keyCount > 999) "999+" else state.keyCount.toString()
                    )
                    // todo: navigate to alarm screen
                    Image(
                        modifier = Modifier.size(30.dp),
                        painter = painterResource(id = if (state.newNotificationArrived) R.drawable.alarm_new else R.drawable.alarm),
                        contentDescription = "alarm",
                    )
                }
                if (state.newTimeCapsuleArrived) {
                    Image(
                        modifier = Modifier
                            .size(30.dp)
                            .clickable {
                                navToArrivedTimeCapsules()
                            },
                        painter = painterResource(id = R.drawable.time_capsule_new),
                        contentDescription = "new time capsule arrived",
                    )
                }
                if (state.newReportArrived) {
                    // todo: navigate to daily report detail screen
                    // todo: get most recently arrived daily report id
                    Image(
                        modifier = Modifier.size(30.dp),
                        painter = painterResource(id = R.drawable.daily_report_new),
                        contentDescription = "new daily report arrived",
                    )
                }
            }

            // home content
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = buildAnnotatedString {
                        append("${state.nickname}님,\n")
                        withStyle(SpanStyle(color = MooiTheme.colorScheme.primary)) {
                            append("오늘의 기분")
                        }
                        append("은 어떤가요?")
                    },
                    style = MooiTheme.typography.head1.copy(fontWeight = FontWeight.SemiBold),
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
                Text(
                    modifier = Modifier.padding(top = 12.dp),
                    text = "대화로 내 감정을 들여다보고\n타임캡슐로 저장해보세요",
                    style = TextStyle(
                        fontFamily = pretendard,
                        fontWeight = FontWeight.Light,
                        fontSize = 17.sp,
                        lineHeight = 24.sp,
                        letterSpacing = (-0.02).em,
                    ),
                    textAlign = TextAlign.Center,
                    color = MooiTheme.colorScheme.gray500
                )


                Column(
                    modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StartChatButton(
                        modifier = Modifier.padding(top = 22.dp),
                        canStartChat = state.ticketCount > 0,
                        onChatStart = {
                            onAction(HomeAction.EnterChat)
                        })

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Image(
                            modifier = Modifier.size(18.dp),
                            painter = painterResource(id = R.drawable.ticket),
                            contentDescription = "ticket",
                            colorFilter = ColorFilter.tint(MooiTheme.colorScheme.secondary)
                        )
                        Text(
                            text = "감정 대화 티켓",
                            style = MooiTheme.typography.body3,
                            color = MooiTheme.colorScheme.secondary
                        )

                        Text(
                            text = "${state.ticketCount}/10",
                            style = MooiTheme.typography.body3,
                            color = MooiTheme.colorScheme.secondary
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
    onChatStart: () -> Unit = {}
) {
    if (canStartChat) {
        // todo: change to use CtaButton component?
        Row(
            modifier = modifier
                .mainBackground(isActivated = true)
                .clickable {
                    onChatStart()
                }
                .padding(vertical = 15.dp, horizontal = 37.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(end = 7.dp),
                text = "대화 시작하기",
                style = MooiTheme.typography.button,
                color = Color.White
            )
            Image(
                modifier = Modifier.size(18.dp),
                painter = painterResource(id = R.drawable.ticket),
                contentDescription = "ticket",
                colorFilter = ColorFilter.tint(Color.White.copy(alpha = 0.7f))
            )
            Text(
                text = "-1",
                style = MooiTheme.typography.button,
                color = Color.White.copy(alpha = 0.7f)
            )
        }
    } else {
        Column(
            modifier = Modifier
                .size(197.dp, 65.dp)
                .background(
                    MooiTheme.colorScheme.gray700, RoundedCornerShape(10.dp)
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "대화 시작하기",
                style = MooiTheme.typography.button,
                color = MooiTheme.colorScheme.gray500
            )
            Text(
                text = "(대화 티켓 부족)",
                style = MooiTheme.typography.body4.copy(lineHeight = 20.sp),
                color = MooiTheme.colorScheme.gray500
            )
        }
    }
}

@Preview
@Composable
private fun StartChatUIPreview() {
    MooiTheme {
        Column(
            modifier = Modifier.background(MooiTheme.colorScheme.background),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StartChatButton()
            StartChatButton(canStartChat = false)
        }
    }
}


@Preview
@Composable
private fun HomeScreenPreview() {
    MooiTheme {
        StatelessHomeScreen(
            state = HomeState(
                nickname = "찡찡이",
                keyCount = 3,
                ticketCount = 5,
                newNotificationArrived = true,
                newTimeCapsuleArrived = true,
                newReportArrived = true
            )
        )
    }
}