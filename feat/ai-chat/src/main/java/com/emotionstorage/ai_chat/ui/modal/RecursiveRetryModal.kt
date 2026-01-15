package com.emotionstorage.ai_chat.ui.modal

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emotionstorage.ui.component.modal.Modal
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun RecursiveRetryModal(navToMainScreen: () -> Unit) {
    Modal(
        title = "계속 시도했지만\n타임캡슐 생성에 실패했어요.\n메인 화면으로 이동합니다.",
        onDismissRequest = {},
        confirmLabel = "메인 화면으로 이동",
        contentPadding = PaddingValues(top = 26.dp, start = 30.dp, end = 30.dp, bottom = 28.dp),
        onConfirm = navToMainScreen,
    )
}

@Preview
@Composable
private fun RecursiveRetryModalPreview() {
    MooiTheme {
        RecursiveRetryModal(
            navToMainScreen = {},
        )
    }
}
