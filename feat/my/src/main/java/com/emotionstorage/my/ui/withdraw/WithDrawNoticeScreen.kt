package com.emotionstorage.my.ui.withdraw

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.emotionstorage.domain.common.ErrorCode
import com.emotionstorage.my.presentation.WithdrawNoticeAction
import com.emotionstorage.my.presentation.WithdrawNoticeEffect
import com.emotionstorage.my.presentation.WithdrawNoticeViewModel
import com.emotionstorage.my.ui.modal.ConfirmWithdrawModal
import com.emotionstorage.my.ui.modal.InquireWithdrawErrorModal
import com.emotionstorage.my.ui.modal.WithdrawSuccessModal
import com.emotionstorage.ui.component.appBar.TopAppBar
import com.emotionstorage.ui.component.button.CtaButton
import com.emotionstorage.ui.component.button.CtaButtonType
import com.emotionstorage.ui.theme.MooiTheme

private sealed class WithdrawNoticeModalState {
    object None : WithdrawNoticeModalState()

    object ConfirmWithdraw : WithdrawNoticeModalState()

    object WithdrawSuccess : WithdrawNoticeModalState()

    data class InquireWithdrawError(
        val errorCode: ErrorCode,
        val throwable: Throwable,
    ) : WithdrawNoticeModalState()
}

@Composable
fun WithDrawNoticeScreen(
    viewModel: WithdrawNoticeViewModel = hiltViewModel(),
    navToBack: () -> Unit = {},
    navToNotificationSetting: () -> Unit = {},
    navToLogin: () -> Unit = {},
) {
    val (modalState, setModalState) =
        remember {
            mutableStateOf<WithdrawNoticeModalState>(WithdrawNoticeModalState.None)
        }

    LaunchedEffect(Unit) {
        viewModel.container.sideEffectFlow.collect { sideEffect ->
            when (sideEffect) {
                is WithdrawNoticeEffect.WithDrawSuccess -> {
                    setModalState(WithdrawNoticeModalState.WithdrawSuccess)
                }

                is WithdrawNoticeEffect.WithdrawError -> {
                    setModalState(
                        WithdrawNoticeModalState.InquireWithdrawError(
                            sideEffect.errorCode,
                            sideEffect.throwable,
                        ),
                    )
                }
            }
        }
    }

    StatelessWithDrawNoticeScreen(
        onBackClick = navToBack,
        setModalState = setModalState,
    )

    when (modalState) {
        is WithdrawNoticeModalState.None -> {
            // no modal
        }

        is WithdrawNoticeModalState.ConfirmWithdraw -> {
            ConfirmWithdrawModal(
                onDismissRequest = {
                    setModalState(WithdrawNoticeModalState.None)
                },
                onChangeNotification = {
                    navToNotificationSetting()
                },
                onWithDraw = {
                    viewModel.onAction(WithdrawNoticeAction.WithDraw)
                },
            )
        }

        is WithdrawNoticeModalState.WithdrawSuccess -> {
            WithdrawSuccessModal(
                onDismissRequest = {
                    setModalState(WithdrawNoticeModalState.None)
                    navToLogin()
                },
            )
        }

        is WithdrawNoticeModalState.InquireWithdrawError -> {
            InquireWithdrawErrorModal(
                errorCode = modalState.errorCode,
                throwable = modalState.throwable,
                onDismissRequest = {
                    setModalState(WithdrawNoticeModalState.None)
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
                    setModalState(WithdrawNoticeModalState.ConfirmWithdraw)
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
