package com.emotionstorage.time_capsule_detail.ui.modal

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.emotionstorage.ui.component.modal.Modal
import kotlinx.coroutines.delay

/**
 * capsule_save_success
 * - Case : 타임캡슐 보관 성공
 *     - 오픈일 설정 후 [타임캡슐 보관하기]를 눌렀을 때 표출
 * - 표시 화면
 *     - 3.6 타임캡슐 - 보관 기간 설정
 *     - 4.6 타임캡슐 - 임시저장 보관 설정
 * - CTA 및 이동
 *     - [메인 화면으로 이동] -> 팝업 닫히며 메인 화면으로 이동
 * - 차단
 *     - 뒤로가기(Android) X
 *     - 배경 터치 X
 */
@Composable
fun TimeCapsuleSavedModal(
    isModalOpen: Boolean = false,
    onConfirm: () -> Unit = {},
) {
    LaunchedEffect(isModalOpen) {
        if (isModalOpen) {
            // confirm automatically after 5 seconds
            delay(5000)
            onConfirm()
        }
    }

    if (isModalOpen) {
        Modal(
            onDismissRequest = {},
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            title =
                "타임캡슐을\n안전하게 보관했어요.",
            bottomDescription = "이 감정은 추후 더\n소중한 이야기가 될 거예요.",
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
