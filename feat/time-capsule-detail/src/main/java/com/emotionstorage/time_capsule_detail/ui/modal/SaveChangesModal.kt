package com.emotionstorage.time_capsule_detail.ui.modal

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.emotionstorage.ui.component.Modal

@Composable
fun SaveChangesModal(
    isModalOpen: Boolean = false,
    onDismissRequest: () -> Unit = {},
    onSave: () -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    if (isModalOpen) {
        Modal(
            title =
                "변경사항이 저장되지 않았어요.\n" +
                    "저장 후 나갈까요?",
            confirmLabel = "네, 저장 후 나갈게요.",
            dismissLabel = "아니요, 그냥 나갈래요.",
            onDismissRequest = onDismissRequest,
            onConfirm = onSave,
            onDismiss = onDismiss,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SaveChangesModalPreview() {
    // background ui
    Box(modifier = Modifier.fillMaxSize())

    SaveChangesModal(
        isModalOpen = true,
    )
}
