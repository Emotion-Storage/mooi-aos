package com.emotionstorage.time_capsule_detail.ui.modal

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.emotionstorage.ui.component.Modal

@Composable
fun TimeCapsuleExpiredModal(
    isModalOpen: Boolean = false,
    onConfirm: () -> Unit = {},
) {
    if (isModalOpen) {
        Modal(
            title =
                "보관 기한이 만료되어\n캡슐을 보관할 수 없어요.",
            bottomDescription = "새로운 감정은 새 타임캡슐에 담아보세요.",
            confirmLabel = "네, 확인했어요.",
            onConfirm = onConfirm,
            onDismissRequest = {
                // do nothing
                // cannot dismiss modal unless confirm button is clicked
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TimeCapsuleExpiredModalPreview() {
    // background ui
    Box(modifier = Modifier.fillMaxSize())

    TimeCapsuleExpiredModal(
        isModalOpen = true,
    )
}
