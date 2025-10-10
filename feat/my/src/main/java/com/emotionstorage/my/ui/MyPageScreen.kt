package com.emotionstorage.my.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.emotionstorage.my.presentation.MyPageAction
import com.emotionstorage.my.presentation.MyPageSideEffect
import com.emotionstorage.my.presentation.MyPageState
import com.emotionstorage.my.presentation.MyPageViewModel
import com.emotionstorage.my.ui.component.KeyCard
import com.emotionstorage.my.ui.component.MenuSection
import com.emotionstorage.my.ui.component.ProfileHeader
import com.emotionstorage.my.ui.component.TempToast
import com.emotionstorage.ui.R
import com.emotionstorage.ui.component.Modal
import com.emotionstorage.ui.theme.MooiTheme
import com.orhanobut.logger.Logger

@Composable
fun MyPageScreen(
    modifier: Modifier = Modifier,
    viewModel: MyPageViewModel = hiltViewModel(),
    navToLogin: () -> Unit = {},
    navToWithdrawNotice: () -> Unit = {},
    navToNickNameChange: () -> Unit = {},
    navToKeyDescription: () -> Unit = {},
    navToAccountInfo: () -> Unit = {},
    navToTermsAndPrivacy: () -> Unit = {},
) {
    val state = viewModel.container.stateFlow.collectAsState()
    var showEmailCopiedToast by remember { mutableStateOf(false) }

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

                is MyPageSideEffect.NavigateToKeyDescription -> {
                    navToKeyDescription()
                }

                is MyPageSideEffect.EmailCopied -> {
                    showEmailCopiedToast = true
                }

                is MyPageSideEffect.ShowToast -> {
                    // todo: add error toast
                }

                is MyPageSideEffect.NavigateToTermsAndPrivacy -> {
                    navToTermsAndPrivacy()
                }

                is MyPageSideEffect.NavigateToWithDrawNotice -> {
                    navToWithdrawNotice()
                }

                is MyPageSideEffect.NavigateToAccountInfo -> {
                    navToAccountInfo()
                }

                else -> {
                    Unit
                }
            }
        }
    }

    StatelessMyPageScreen(
        modifier = modifier,
        state = state.value,
        showEmailCopiedToast = showEmailCopiedToast,
        onAction = viewModel::onAction,
        navToWithdraw = navToWithdrawNotice,
        navToNickNameChange = navToNickNameChange,
        navToKeyDescription = navToKeyDescription,
        navToAccountInfo = navToAccountInfo,
        navToTermsAndPrivacy = navToTermsAndPrivacy,
        onToastDismissed = { showEmailCopiedToast = false },
    )
}

@Composable
private fun StatelessMyPageScreen(
    modifier: Modifier = Modifier,
    state: MyPageState = MyPageState(),
    showEmailCopiedToast: Boolean = false,
    onAction: (MyPageAction) -> Unit = {},
    navToWithdraw: () -> Unit = {},
    navToNickNameChange: () -> Unit = {},
    navToKeyDescription: () -> Unit = {},
    navToAccountInfo: () -> Unit = {},
    navToTermsAndPrivacy: () -> Unit = {},
    onToastDismissed: () -> Unit = {},
) {
    val clipboardManager = LocalClipboardManager.current
    val snackbarHostState = remember { SnackbarHostState() }

    var showLogoutModal by remember { mutableStateOf(false) }

    LaunchedEffect(showEmailCopiedToast) {
        if (showEmailCopiedToast) {
            snackbarHostState.showSnackbar(
                message = "",
                duration = SnackbarDuration.Short,
            )
            onToastDismissed()
        }
    }

    Scaffold(
        modifier
            .fillMaxSize()
            .background(MooiTheme.colorScheme.background),
        snackbarHost = {
            SnackbarHost(snackbarHostState) {
                TempToast(
                    modifier = Modifier,
                    message = "이메일이 복사되었습니다.",
                    resId = R.drawable.mail,
                )
            }
        },
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(MooiTheme.colorScheme.background)
                    .padding(innerPadding)
                    .padding(start = 16.dp, end = 16.dp, top = 35.dp)
                    .consumeWindowInsets(WindowInsets.navigationBars),
            verticalArrangement = Arrangement.Top,
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
                    onAction(MyPageAction.CopyEmail)
                },
                onTermsAndPrivacyClick = navToTermsAndPrivacy,
                onLogoutClick = { showLogoutModal = true },
            )

            if (showLogoutModal) {
                Modal(
                    title = "정말 로그아웃 하시겠어요?",
                    confirmLabel = "아니요, 그냥 있을래요.",
                    onDismissRequest = { showLogoutModal = false },
                    topDescription = null,
                    bottomDescription = "다시 돌아오실거죠? 기다리고있을게요 \uD83E\uDD7A",
                    dismissLabel = "네, 로그아웃 할래요.",
                    onDismiss = { onAction(MyPageAction.Logout) },
                    onConfirm = { showLogoutModal = false },
                )
            }

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
                style = MooiTheme.typography.caption7.copy(lineHeight = 24.sp),
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
