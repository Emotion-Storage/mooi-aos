package com.emotionstorage.tutorial.ui.modal

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.emotionstorage.ui.component.modal.Modal

@Composable
fun RetryLoginModal(
    onConfirm: () -> Unit,
) {
    Modal(
        onDismissRequest = {
            // disable bg click to dismiss
        },
        disableBackPress = true,
        topDescription = "가입은 정상적으로 완료되었어요!",
        title = "하지만 로그인 연결이 잠시\n불안정해 다시 시도해야해요.",
        confirmLabel = "다시 로그인하기",
        onConfirm = onConfirm,
    )
}

@Preview
@Composable
private fun RetryLoginModalPreview() {
    RetryLoginModal(
        onConfirm = {},
    )
}
