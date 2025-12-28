package com.emotionstorage.my.ui.myPage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.emotionstorage.my.presentation.MyPageAction
import com.emotionstorage.my.presentation.MyPageSideEffect
import com.emotionstorage.my.presentation.MyPageState
import com.emotionstorage.my.presentation.MyPageViewModel
import com.emotionstorage.my.ui.keyDescription.component.KeyCard
import com.emotionstorage.my.ui.modal.ConfirmLogoutModal
import com.emotionstorage.my.ui.modal.LogoutErrorModal
import com.emotionstorage.my.ui.myPage.component.MenuSection
import com.emotionstorage.my.ui.myPage.component.ProfileHeader
import com.emotionstorage.ui.component.loading.LoadingOverlay
import com.emotionstorage.ui.theme.MooiTheme
import com.orhanobut.logger.Logger

private enum class MyPageModalState {
    NONE,
    LOGOUT_CONFIRM,
    LOGOUT_ERROR,
}

@Composable
fun MyPageScreen(
    modifier: Modifier = Modifier,
    bottomAppBar: @Composable () -> Unit = {},
    viewModel: MyPageViewModel = hiltViewModel(),
    navToLogin: () -> Unit = {},
    navToWithdrawNotice: () -> Unit = {},
    navToNickNameChange: () -> Unit = {},
    navToKeyDescription: () -> Unit = {},
    navToAccountInfo: () -> Unit = {},
    navToTermsAndPrivacy: () -> Unit = {},
    navToNotificationSetting: () -> Unit = {},
) {
    val state = viewModel.container.stateFlow.collectAsState()
    val (modalState, setModalState) = remember { mutableStateOf<MyPageModalState>(MyPageModalState.NONE) }

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

                is MyPageSideEffect.LogoutError -> {
                    setModalState(MyPageModalState.LOGOUT_ERROR)
                }

                is MyPageSideEffect.ShowToast -> {
                    // todo: add error toast
                }
            }
        }
    }

    StatelessMyPageScreen(
        modifier = modifier,
        bottomAppBar = bottomAppBar,
        setModalState = setModalState,
        state = state.value,
        navToWithdraw = navToWithdrawNotice,
        navToNickNameChange = navToNickNameChange,
        navToKeyDescription = navToKeyDescription,
        navToAccountInfo = navToAccountInfo,
        navToTermsAndPrivacy = navToTermsAndPrivacy,
        navToNotificationSetting = navToNotificationSetting,
    )

    when (modalState) {
        MyPageModalState.NONE -> {
            // no modal
        }

        MyPageModalState.LOGOUT_CONFIRM -> {
            ConfirmLogoutModal(
                onDismissRequest = {
                    setModalState(MyPageModalState.NONE)
                },
                onLogout = { viewModel.onAction(MyPageAction.Logout) }
            )
        }

        MyPageModalState.LOGOUT_ERROR -> {
            LogoutErrorModal(
                onDismissRequest = {
                    setModalState(MyPageModalState.NONE)
                },
                onRetry = {
                    viewModel.onAction(MyPageAction.Logout)
                }
            )
        }
    }
}

@Composable
private fun StatelessMyPageScreen(
    modifier: Modifier = Modifier,
    bottomAppBar: @Composable () -> Unit = {},
    setModalState: (MyPageModalState) -> Unit = {},
    state: MyPageState = MyPageState(),
    navToWithdraw: () -> Unit = {},
    navToNickNameChange: () -> Unit = {},
    navToKeyDescription: () -> Unit = {},
    navToAccountInfo: () -> Unit = {},
    navToTermsAndPrivacy: () -> Unit = {},
    navToNotificationSetting: () -> Unit = {},
) {
    val clipboardManager = LocalClipboardManager.current

    Scaffold(
        modifier =
            modifier
                .fillMaxSize()
                .padding(top = 16.dp)
                .background(MooiTheme.colorScheme.backgroundDefault),
        bottomBar = bottomAppBar,
    ) { innerPadding ->

        Box {
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(MooiTheme.colorScheme.backgroundDefault)
                        .padding(innerPadding)
                        .padding(start = 16.dp, end = 16.dp, top = 35.dp)
                        .consumeWindowInsets(WindowInsets.navigationBars),
                verticalArrangement = Arrangement.Top,
            ) {
                ProfileHeader(
                    modifier = Modifier.offset(x = (-9).dp),
                    nickname = state.nickname,
                    signupDday = state.signupDday,
                    onEditClick = {
                        navToNickNameChange()
                    },
                )

                Spacer(modifier = Modifier.size(16.dp))

                KeyCard(
                    keyCount = state.keyCount,
                ) {
                    navToKeyDescription()
                }

                Spacer(modifier = Modifier.size(24.dp))

                MenuSection(
                    versionInfo = state.versionName,
                    onAccountInfoClick = navToAccountInfo,
                    onEmailCopyClick = {
                        clipboardManager.setText(AnnotatedString("mooi.reply@gmail.com"))
                    },
                    onTermsAndPrivacyClick = navToTermsAndPrivacy,
                    onLogoutClick = { setModalState(MyPageModalState.LOGOUT_CONFIRM) },
                    onNotificationClick = navToNotificationSetting,
                )

                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = "계정 탈퇴하기",
                    modifier =
                        Modifier
                            .wrapContentSize()
                            .align(alignment = Alignment.End)
                            .clickable { navToWithdraw() },
                    textAlign = TextAlign.End,
                    color = MooiTheme.colorScheme.gray600,
                    style = MooiTheme.typography.caption7,
                )
            }

            if (state.nickname.isBlank()) {
                LoadingOverlay(
                    modifier = Modifier.align(Alignment.Center),
                    delayDuration = 0L,
                )
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
