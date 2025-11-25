package com.emotionstorage.ai_chat.ui.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emotionstorage.ui.component.Modal
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun NetworkErrorModal(onRetry: () -> Unit) {
    Modal(
        title = "감정을 정리하는 중에\n잠깐 연결이 끊긴 것 같아요.\n다시 시도해볼까요?",
        onDismissRequest = {},
        confirmLabel = "다시 시도하기",
        contentPadding = PaddingValues(top = 26.dp, start = 36.dp, end = 36.dp, bottom = 28.dp),
        onConfirm = onRetry,
    )
}

@Preview
@Composable
private fun NetworkModalPreview() {
    MooiTheme {
        NetworkErrorModal(
            onRetry = {},
        )
    }
}
