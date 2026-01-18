package com.emotionstorage.home.ui.modal

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.emotionstorage.ui.component.modal.Modal
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun AttendanceRefreshModal(onConfirm: () -> Unit) {
    Modal(
        onDismissRequest = {},
        dismissOnBackPress = false,
        dismissOnClickOutside = false,
        title = "날짜가 바뀌었어요!",
        bottomDescription = "오늘의 출석 보상은\n새로고침 후 다시 확인해주세요.",
        confirmLabel = "새로운 보상 확인하기",
        onConfirm = onConfirm,
    )
}

@Preview
@Composable
private fun AttendanceRefreshModalPreview() {
    MooiTheme {
        AttendanceRefreshModal(
            onConfirm = {},
        )
    }
}
