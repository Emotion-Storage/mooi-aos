package com.emotionstorage.my.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.emotionstorage.my.presentation.MyPageAction
import com.emotionstorage.my.presentation.MyPageSideEffect
import com.emotionstorage.my.presentation.MyPageState
import com.emotionstorage.my.presentation.MyPageViewModel
import com.emotionstorage.ui.theme.MooiTheme
import com.orhanobut.logger.Logger

@Composable
fun MyPageScreen(
    modifier: Modifier = Modifier,
    viewModel: MyPageViewModel = hiltViewModel(),
    navToLogin: () -> Unit = {},
    navToWithdraw: () -> Unit = {},
) {
    val state = viewModel.container.stateFlow.collectAsState()

    LifecycleResumeEffect(Unit) {
        Logger.d("MyPageScreen: onResume triggered")
        viewModel.onAction(MyPageAction.Initiate)
        onPauseOrDispose {
            // do nothing
        }
    }

    LaunchedEffect(Unit) {
        viewModel.container.sideEffectFlow.collect { sideEffect ->
            when (sideEffect) {
                is MyPageSideEffect.LogoutSuccess -> {
                    navToLogin()
                }

                is MyPageSideEffect.ShowToast -> {
                    // todo: add error toast
                }
            }
        }
    }

    StatelessMyPageScreen(
        modifier = modifier,
        state = state.value,
        onAction = viewModel::onAction,
        navToWithdraw = navToWithdraw
    )
}

@Composable
private fun StatelessMyPageScreen(
    modifier: Modifier = Modifier,
    state: MyPageState = MyPageState(),
    onAction: (MyPageAction) -> Unit = {},
    navToWithdraw: () -> Unit = {},
) {
    val clipboardManager = LocalClipboardManager.current

    Scaffold(
        modifier
            .fillMaxSize()
            .background(MooiTheme.colorScheme.background)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MooiTheme.colorScheme.background)
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(text = "${state.nickname}님", color = Color.White)
            Text(text = "MOOI와 함께한 지 +${state.signupDday}일", color = Color.White)
            Text(text = "보유 열쇠 ${state.keyCount}개", color = Color.White)
            Text(text = "버전 정보 ${state.versionName}", color = Color.White)
            Text(
                modifier = Modifier.clickable {
                    // Copy reply email to clipboard
                    clipboardManager.setText(AnnotatedString(state.replyEmail))
                },
                text = "MOOI에게 의견 보내기\n${state.replyEmail}",
                color = Color.White
            )

            Button(
                onClick = {
                    // todo: add confirm modal
                    onAction(MyPageAction.Logout)
                }
            ) {
                Text("로그아웃")
            }
            Button(
                onClick = {
                    navToWithdraw()
                }
            ) {
                Text("탈퇴하기")
            }

        }
    }
}

@Preview
@Composable
private fun MyPageScreenPreview() {
    MooiTheme {
        StatelessMyPageScreen()
    }
}