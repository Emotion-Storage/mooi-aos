package com.emotionstorage.time_capsule_detail.ui.modal

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.emotionstorage.ui.component.Modal

@Composable
fun DeleteTimeCapsuleModal(
    isModalOpen: Boolean = false,
    onDismissRequest: () -> Unit = {},
    onDelete: () -> Unit = {},
) {
    if (isModalOpen) {
        Modal(
            title =
                "타임캡슐은 삭제하면\n" +
                    "다시 되돌릴 수 없어요.\n" +
                    "그래도 삭제할까요?",
            confirmLabel = "아니요, 유지할게요.",
            dismissLabel = "네, 삭제할게요.",
            onDismissRequest = onDismissRequest,
            onDismiss = onDelete,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DeleteTimeCapsuleModalPreview() {
    // background ui
    Box(modifier = Modifier.fillMaxSize())

    DeleteTimeCapsuleModal(
        isModalOpen = true,
    )
}
