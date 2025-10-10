package com.emotionstorage.time_capsule_detail.ui.modal

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.emotionstorage.ui.component.Modal
import kotlinx.coroutines.delay

@Composable
fun TimeCapsuleSavedModal(
    isModalOpen: Boolean = false,
    onConfirm: () -> Unit = {},
) {
    LaunchedEffect(isModalOpen) {
        if (isModalOpen)
            {
                // confirm automatically after 5 seconds
                delay(5000)
                onConfirm()
            }
    }

    if (isModalOpen) {
        Modal(
            title =
                "타임캡슐을\n안전하게 보관했어요.",
            bottomDescription = "이 감정은 추후 더\n소중한 이야기가 될 거예요.",
            onDismissRequest = {},
            confirmLabel = "네, 확인했어요.",
            onConfirm = onConfirm,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TimeCapsuleSavedModalPreview() {
    // background ui
    Box(modifier = Modifier.fillMaxSize())

    TimeCapsuleSavedModal(
        isModalOpen = true,
    )
}
