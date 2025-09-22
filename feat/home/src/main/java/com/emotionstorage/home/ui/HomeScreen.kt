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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import com.emotionstorage.ui.theme.MooiTheme
import com.emotionstorage.ui.theme.pretendard
import com.emotionstorage.ui.util.mainBackground

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    navToChat: (roomId: String) -> Unit = {}
) {
    val state = viewModel.container.stateFlow.collectAsState()

    LaunchedEffect(Unit) {
        // init nickname on launch
        viewModel.onAction(HomeAction.InitNickName)
    }

    LifecycleResumeEffect(Unit) {
        // update screen state on resume
        viewModel.onAction(HomeAction.UpdateState)
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
    )
}

@Composable
private fun StatelessHomeScreen(
    modifier: Modifier = Modifier,
    state: HomeState = HomeState(),
    onAction: (HomeAction) -> Unit = {},
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
                .padding(innerPadding)
        ) {
            // todo: add icons

            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "${state.nickname}님,\n오늘의 기분은 어떤가요?",
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
                nickname = "찡찡이"
            )
        )
    }
}