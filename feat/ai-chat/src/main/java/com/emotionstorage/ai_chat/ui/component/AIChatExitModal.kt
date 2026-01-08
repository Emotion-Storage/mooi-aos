package com.emotionstorage.ai_chat.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.emotionstorage.ui.component.modal.Modal
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun AIChatExitModal(
    isModalOpen: Boolean = false,
    onDismissRequest: () -> Unit = {},
    onContinue: () -> Unit = {},
    onExit: () -> Unit = {},
) {
    if (isModalOpen) {
        Modal(
            title = "잠시 감정 대화를\n이대로 중지할까요?",
            bottomDescription = "오늘의 감정 대화는\n오늘까지만 임시저장돼요!",
            bottomDescriptionHighlights = listOf("오늘까지만"),
            confirmLabel = "대화를 계속 진행할게요.",
            dismissLabel = "메인 화면으로 나갈래요.",
            onDismissRequest = onDismissRequest,
            onConfirm = onContinue,
            onDismiss = onExit,
        )
    }
}

@Preview
@Composable
private fun AIChatExitModalPreview() {
    MooiTheme {
        AIChatExitModal(isModalOpen = true)
    }
}
