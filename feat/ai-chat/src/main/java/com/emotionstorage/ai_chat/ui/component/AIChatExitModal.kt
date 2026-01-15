package com.emotionstorage.ai_chat.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.emotionstorage.ui.component.modal.Modal
import com.emotionstorage.ui.theme.MooiTheme

/**
 * dialog_leave
 * - Case : 대화 중단하기
 *     - 대화 중 뒤로가기(스와이프 포함)를 시도했을 때 표출
 * - 표시 화면
 *     - 3.3 ai 감정대화
 * - CTA 및 이동
 *     - [대화를 계속 진행할게요.] -> 팝업 닫히며 이동 X
 *     - [메인 화면으로 나갈래요.] -> 팝업 닫히며 메인 화면으로 이동(중단된 대화는 임시 저장 처리)
 * - 차단
 *     - 뒤로가기(Android) O (팝업 닫힘, 화면 제자리)
 *     - 배경 터치 O (팝업 닫힘, 화면 제자리)
 */
@Composable
fun AIChatExitModal(
    isModalOpen: Boolean = false,
    onDismissRequest: () -> Unit = {},
    onContinue: () -> Unit = {},
    onExit: () -> Unit = {},
) {
    if (isModalOpen) {
        Modal(
            title = "잠시 감정 대화를\n이대로 중지할까요?",
            bottomDescription = "오늘의 감정 대화는\n오늘까지만 임시저장돼요!",
            bottomDescriptionHighlights = listOf("오늘까지만"),
            confirmLabel = "대화를 계속 진행할게요.",
            dismissLabel = "메인 화면으로 나갈래요.",
            onDismissRequest = onDismissRequest,
            onConfirm = onContinue,
            onDismiss = onExit,
        )
    }
}

@Preview
@Composable
private fun AIChatExitModalPreview() {
    MooiTheme {
        AIChatExitModal(isModalOpen = true)
    }
}
