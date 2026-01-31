package com.emotionstorage.my.ui.withdraw

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.emotionstorage.domain.common.ErrorCode
import com.emotionstorage.my.presentation.WithdrawNoticeAction
import com.emotionstorage.my.presentation.WithdrawNoticeEffect
import com.emotionstorage.my.presentation.WithdrawNoticeViewModel
import com.emotionstorage.my.ui.modal.ConfirmWithdrawModal
import com.emotionstorage.my.ui.modal.InquireWithdrawErrorModal
import com.emotionstorage.my.ui.modal.WithdrawSuccessModal
import com.emotionstorage.ui.annotation.PreviewScreenRatios
import com.emotionstorage.ui.component.appBar.TopAppBar
import com.emotionstorage.ui.component.button.CtaButton
import com.emotionstorage.ui.component.button.CtaButtonType
import com.emotionstorage.ui.component.loading.LoadingOverlay
import com.emotionstorage.ui.theme.MooiTheme

private sealed class ModalState {
    object None : ModalState()

    object ConfirmWithdraw : ModalState()

    object WithdrawSuccess : ModalState()

    data class InquireWithdrawError(
        val errorCode: ErrorCode,
        val throwable: Throwable,
        val userEmail: String? = null,
        val userNickname: String? = null,
    ) : ModalState()
}

@Composable
fun WithDrawNoticeScreen(
    viewModel: WithdrawNoticeViewModel = hiltViewModel(),
    navToBack: () -> Unit = {},
    navToLogin: () -> Unit = {},
//    navToNotificationSetting: () -> Unit = {},
) {
    val state by viewModel.container.stateFlow.collectAsState()
    val (modalState, setModalState) =
        remember {
            mutableStateOf<ModalState>(ModalState.None)
        }

    LaunchedEffect(Unit) {
        viewModel.container.sideEffectFlow.collect { sideEffect ->
            when (sideEffect) {
                is WithdrawNoticeEffect.WithDrawSuccess -> {
                    setModalState(ModalState.WithdrawSuccess)
                }

                is WithdrawNoticeEffect.WithdrawError -> {
                    setModalState(
                        ModalState.InquireWithdrawError(
                            sideEffect.errorCode,
                            sideEffect.throwable,
                            sideEffect.userEmail,
                            sideEffect.userNickname,
                        ),
                    )
                }
            }
        }
    }

    if (state.isLoading) {
        LoadingOverlay()
    }

    StatelessWithDrawNoticeScreen(
        onBackClick = navToBack,
        setModalState = setModalState,
    )

    when (modalState) {
        is ModalState.None -> {
            // no modal
        }

        is ModalState.ConfirmWithdraw -> {
            ConfirmWithdrawModal(
                onDismissRequest = {
                    setModalState(ModalState.None)
                },
                onWithDraw = {
                    viewModel.onAction(WithdrawNoticeAction.WithDraw)
                },
               /* onChangeNotification = {
                    navToNotificationSetting()
                },*/
            )
        }

        is ModalState.WithdrawSuccess -> {
            WithdrawSuccessModal(
                onDismissRequest = {
                    setModalState(ModalState.None)
                    navToLogin()
                },
            )
        }

        is ModalState.InquireWithdrawError -> {
            InquireWithdrawErrorModal(
                errorCode = modalState.errorCode,
                throwable = modalState.throwable,
                userEmail = modalState.userEmail,
                userNickname = modalState.userNickname,
                onDismissRequest = {
                    setModalState(ModalState.None)
                },
            )
        }
    }
}

@Composable
private fun StatelessWithDrawNoticeScreen(
    onBackClick: () -> Unit,
    setModalState: (ModalState) -> Unit,
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
                    setModalState(ModalState.ConfirmWithdraw)
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

@PreviewScreenRatios
@Composable
private fun WithDrawNoticeScreenPreview() {
    MooiTheme {
        StatelessWithDrawNoticeScreen(
            onBackClick = {},
            setModalState = {},
        )
    }
}
