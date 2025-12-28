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
import com.emotionstorage.my.ui.modal.ConfirmWithdrawModal
import com.emotionstorage.my.ui.modal.WithdrawSuccessModal
import com.emotionstorage.ui.component.modal.Modal
import com.emotionstorage.ui.component.appBar.TopAppBar
import com.emotionstorage.ui.component.button.CtaButton
import com.emotionstorage.ui.component.button.CtaButtonType
import com.emotionstorage.ui.theme.MooiTheme
import kotlinx.coroutines.delay

private enum class WithdrawNoticeModalState {
    NONE,
    CONFIRM_WITHDRAW,
    WITHDRAW_SUCCESS,
}

@Composable
fun WithDrawNoticeScreen(
    viewModel: MyPageViewModel = hiltViewModel(),
    navToBack: () -> Unit = {},
    navToNotificationSetting: () -> Unit = {},
    navToLogin: () -> Unit = {},
) {
    val (modalState, setModalState) = remember {
        mutableStateOf<WithdrawNoticeModalState>(WithdrawNoticeModalState.NONE)
    }

    LaunchedEffect(Unit) {
        viewModel.container.sideEffectFlow.collect { sideEffect ->
            when (sideEffect) {
                is MyPageSideEffect.WithDrawSuccess -> {
                    setModalState(WithdrawNoticeModalState.WITHDRAW_SUCCESS)
                }

                else -> {
                    Unit
                }
            }
        }
    }

    StatelessWithDrawNoticeScreen(
        onBackClick = navToBack,
        setModalState = setModalState,
    )

    when(modalState){
        WithdrawNoticeModalState.NONE -> {
            // no modal
        }
        WithdrawNoticeModalState.CONFIRM_WITHDRAW -> {
            ConfirmWithdrawModal(
                onDismissRequest = {
                    setModalState(WithdrawNoticeModalState.NONE)
                },
                onChangeNotification = {
                    navToNotificationSetting()
                },
                onWithDraw = {
                    viewModel.onAction(MyPageAction.WithDraw)
                },
            )
        }
        WithdrawNoticeModalState.WITHDRAW_SUCCESS -> {
            WithdrawSuccessModal(
                onDismissRequest = {
                    setModalState(WithdrawNoticeModalState.NONE)
                    navToLogin()
                },
            )
        }
    }
}

@Composable
private fun StatelessWithDrawNoticeScreen(
    onBackClick: () -> Unit,
    setModalState: (WithdrawNoticeModalState) -> Unit,
) {
    Scaffold(
        modifier =
            Modifier
                .fillMaxSize()
                .background(MooiTheme.colorScheme.backgroundDefault),
        topBar = {
            TopAppBar(
                showBackButton = true,
                title = "회원탈퇴",
                onBackClick = onBackClick,
            )
        },
    ) { innerPadding ->
        Box(
            Modifier
                .fillMaxSize()
                .background(MooiTheme.colorScheme.backgroundDefault)
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
                onClick = {
                    setModalState(WithdrawNoticeModalState.CONFIRM_WITHDRAW)
                },
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
            onBackClick = {},
            setModalState = {},
        )
    }
}
