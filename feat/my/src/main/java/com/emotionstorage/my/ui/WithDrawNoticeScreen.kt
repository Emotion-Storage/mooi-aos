package com.emotionstorage.my.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.emotionstorage.my.presentation.MyPageAction
import com.emotionstorage.my.presentation.MyPageSideEffect
import com.emotionstorage.my.presentation.MyPageViewModel
import com.emotionstorage.my.ui.component.WithDrawNoticeContent
import com.emotionstorage.ui.theme.MooiTheme
import kotlinx.coroutines.delay

@Composable
fun WithDrawNoticeScreen(
    viewModel: MyPageViewModel = hiltViewModel(),
    navToBack: () -> Unit = {},
    navToSplash: () -> Unit = {},
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

                else -> Unit
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
            navToSplash()
        }
    }

    StatelessWithDrawNoticeScreen(
        showSuggestDialog = showSuggestDialog,
        showFinalConfirmDialog = showDoneDialog,
        onBackClick = navToBack,
        onWithDrawButtonClick = { showSuggestDialog = true },
        onKeepClick = { showSuggestDialog = false },
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
    onWithDrawButtonClick: () -> Unit,
    onBackClick: () -> Unit,
    onKeepClick: () -> Unit,
    onWithDrawClick: () -> Unit,
    onFinalConfirmClick: () -> Unit,
) {
    WithDrawNoticeContent(
        showSuccessDialog = showSuggestDialog,
        showFinalConfirmDialog = showFinalConfirmDialog,
        onWithDrawButtonClick = onWithDrawButtonClick,
        onKeepClick = onKeepClick,
        onBackClick = onBackClick,
        onWithDrawClick = onWithDrawClick,
        onFinalConfirmClick = onFinalConfirmClick,
        onDismiss = {},
    )
}

@Preview
@Composable
private fun WithDrawNoticeScreenPreview() {
    MooiTheme {
        WithDrawNoticeScreen()
    }
}
