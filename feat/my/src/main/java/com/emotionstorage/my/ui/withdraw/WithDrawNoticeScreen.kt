package com.emotionstorage.my.ui.withdraw

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.emotionstorage.my.presentation.MyPageAction
import com.emotionstorage.my.presentation.MyPageSideEffect
import com.emotionstorage.my.presentation.MyPageViewModel
import com.emotionstorage.ui.component.Modal
import com.emotionstorage.ui.component.appBar.TopAppBar
import com.emotionstorage.ui.component.button.CtaButton
import com.emotionstorage.ui.component.button.CtaButtonType
import com.emotionstorage.ui.theme.MooiTheme
import kotlinx.coroutines.delay

@Composable
fun WithDrawNoticeScreen(
    viewModel: MyPageViewModel = hiltViewModel(),
    navToBack: () -> Unit = {},
    navToNotificationSetting: () -> Unit = {},
    navToLogin: () -> Unit = {},
) {
    var showSuggestDialog by remember { mutableStateOf(false) }
    var showDoneDialog by remember { mutableStateOf(false) }
    var pendingNavigate by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.container.sideEffectFlow.collect { sideEffect ->
            when (sideEffect) {
                is MyPageSideEffect.WithDrawSuccess -> {
                    showDoneDialog = true
                }

                else -> {
                    Unit
                }
            }
        }
    }

    LaunchedEffect(showDoneDialog) {
        if (showDoneDialog) {
            delay(5_000)
            if (showDoneDialog) {
                pendingNavigate = true
                showDoneDialog = false
            }
        }
    }

    LaunchedEffect(showDoneDialog, pendingNavigate) {
        if (!showDoneDialog && pendingNavigate) {
            delay(250)
            pendingNavigate = false
            navToLogin()
        }
    }

    StatelessWithDrawNoticeScreen(
        showSuggestDialog = showSuggestDialog,
        showFinalConfirmDialog = showDoneDialog,
        onBackClick = navToBack,
        onSuggestDismiss = { showSuggestDialog = false },
        onWithDrawButtonClick = { showSuggestDialog = true },
        onKeepClick = {
            showSuggestDialog = false
            navToNotificationSetting()
        },
        onWithDrawClick = {
            showSuggestDialog = false
            viewModel.onAction(MyPageAction.WithDrawConfirm)
        },
        onFinalConfirmClick = {
            pendingNavigate = true
            showDoneDialog = false
        },
    )
}

@Composable
fun StatelessWithDrawNoticeScreen(
    showSuggestDialog: Boolean,
    showFinalConfirmDialog: Boolean,
    onSuggestDismiss: () -> Unit,
    onWithDrawButtonClick: () -> Unit,
    onBackClick: () -> Unit,
    onKeepClick: () -> Unit,
    onWithDrawClick: () -> Unit,
    onFinalConfirmClick: () -> Unit,
) {
    Scaffold(
        modifier =
            Modifier
                .fillMaxSize()
                .background(MooiTheme.colorScheme.background),
        topBar = {
            TopAppBar(
                showBackButton = true,
                title = "회원탈퇴",
                onBackClick = onBackClick,
            )
        },
    ) { innerPadding ->
        if (showSuggestDialog) {
            Modal(
                title = "기록을 잠시 멈추고 싶다면,\n알림을 끄거나\n앱을 쉬어가보는 건 어떨까요?",
                confirmLabel = "알림을 끄고 쉬어갈래요.",
                dismissLabel = "서비스를 탈퇴할래요.",
                onDismissRequest = onSuggestDismiss,
                onConfirm = onKeepClick,
                onDismiss = onWithDrawClick,
                topDescription = null,
            )
        }

        if (showFinalConfirmDialog) {
            Modal(
                title = "회원 탈퇴가\n완료되었습니다.",
                confirmLabel = "확인",
                onDismissRequest = {
                    // cannot dismiss manually
                },
                onConfirm = onFinalConfirmClick,
                topDescription = null,
            )
        }

        Box(
            Modifier
                .fillMaxSize()
                .background(MooiTheme.colorScheme.background)
                .padding(innerPadding),
        ) {
            WithDrawNoticeContent(
                modifier = Modifier.align(Alignment.TopCenter),
            )

            CtaButton(
                modifier =
                    Modifier
                        .align(Alignment.BottomCenter)
                        .padding(start = 16.dp, end = 16.dp, bottom = 28.dp),
                type = CtaButtonType.OUTLINED,
                labelString = "MOOI 서비스 탈퇴하기",
                onClick = onWithDrawButtonClick,
                isDefaultWidth = false,
                textStyle =
                    MooiTheme.typography.body7.copy(
                        color = Color(0xFF979797),
                    ),
            )
        }
    }
}

@Preview
@Composable
private fun WithDrawNoticeScreenPreview() {
    MooiTheme {
        StatelessWithDrawNoticeScreen(
            showSuggestDialog = true,
            showFinalConfirmDialog = false,
            onSuggestDismiss = {},
            onWithDrawButtonClick = {},
            onBackClick = {},
            onKeepClick = {},
            onWithDrawClick = {},
            onFinalConfirmClick = {},
        )
    }
}
