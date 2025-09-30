package com.emotionstorage.time_capsule_detail.ui.component

import androidx.compose.runtime.Composable
import com.emotionstorage.ui.component.Modal

@Composable
fun TimeCapsuleDeleteModel(
    isModelOpen: Boolean = false,
    onDismissRequest: () -> Unit = {},
    onDelete: () -> Unit = {},
) {
    if (isModelOpen) {
        Modal(
            title =
                "타임캡슐은 삭제하면\n" +
                    "다시 되돌릴 수 없어요.\n" +
                    "그래도 삭제할까요?",
            confirmLabel = "아니요, 유지할게요",
            dismissLabel = "네, 삭제할게요",
            onDismissRequest = onDismissRequest,
            onDismiss = onDelete,
        )
    }
}
