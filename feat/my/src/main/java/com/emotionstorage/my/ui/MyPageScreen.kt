package com.emotionstorage.my.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.emotionstorage.my.presentation.MyPageAction
import com.emotionstorage.my.presentation.MyPageSideEffect
import com.emotionstorage.my.presentation.MyPageState
import com.emotionstorage.my.presentation.MyPageViewModel
import com.emotionstorage.my.ui.component.ProfileHeader
import com.emotionstorage.ui.theme.MooiTheme
import com.orhanobut.logger.Logger

@Composable
fun MyPageScreen(
    modifier: Modifier = Modifier,
    viewModel: MyPageViewModel = hiltViewModel(),
    navToLogin: () -> Unit = {},
    navToWithdraw: () -> Unit = {},
    navToNickNameChange: () -> Unit = {},
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

                is MyPageSideEffect.NavigateToNicknameChange -> {
                    navToNickNameChange()
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
        navToWithdraw = navToWithdraw,
        navToNickNameChange = navToNickNameChange
    )
}

@Composable
private fun StatelessMyPageScreen(
    modifier: Modifier = Modifier,
    state: MyPageState = MyPageState(),
    onAction: (MyPageAction) -> Unit = {},
    navToWithdraw: () -> Unit = {},
    navToNickNameChange: () -> Unit = {},
) {
    val clipboardManager = LocalClipboardManager.current

    Scaffold(
        modifier
            .fillMaxSize()
            .background(MooiTheme.colorScheme.background),
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(MooiTheme.colorScheme.background)
                    .padding(innerPadding)
                    .padding(start = 16.dp, end = 16.dp, top = 35.dp, bottom = 44.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            ProfileHeader(
                profileImage = "Glide or Coil이 필요해 보인다",
                nickname = state.nickname,
                signupDday = state.signupDday,
                onEditClick = {
                    navToNickNameChange()
                },
                onProfileClick = {
                    // TODO 이미지 변경 기능 구현 필요
                },
            )
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
