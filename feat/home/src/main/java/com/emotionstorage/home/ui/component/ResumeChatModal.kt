package com.emotionstorage.home.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.emotionstorage.ui.component.modal.Modal
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun ResumeChatModal(
    isModalOpen: Boolean,
    onDismissRequest: () -> Unit = {},
    onResume: () -> Unit = {},
    onDropAndStartNew: () -> Unit = {},
) {
    if (isModalOpen) {
        Modal(
            title = "이전 감정 대화를\n마치지 않았어요.\n이어서 계속 대화할까요?",
            bottomDescription = "'그만하기'를 누르면 이전 대화는 삭제돼요.",
            confirmLabel = "대화를 이어서 진행할게요.",
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            dismissLabel = "이전 대화를 그만할래요.",
            onDismissRequest = onDismissRequest,
            onConfirm = onResume,
            onDismiss = onDropAndStartNew,
        )
    }
}

@Preview
@Composable
private fun ResumeChatModalPreview() {
    MooiTheme {
        ResumeChatModal(isModalOpen = true)
    }
}
