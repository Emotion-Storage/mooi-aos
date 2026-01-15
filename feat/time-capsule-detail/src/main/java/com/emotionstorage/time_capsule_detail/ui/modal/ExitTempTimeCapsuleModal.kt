package com.emotionstorage.time_capsule_detail.ui.modal

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.emotionstorage.ui.component.modal.Modal

/**
 * capsule_save_exit
 * - Case : 타임캡슐 보관 중 나가기(임시저장 confirm)
 *     - 타임캡슐 생성 후 상세페이지에서 뒤로가기를 시도했을 때 표출
 * - 표시 화면
 *     - 3.5 타임캡슐 - 생성된 타임캡슐
 * - CTA 및 이동
 *     - [아니요, 계속할래요.] -> 팝업 닫히며 이동 X
 *     - [네, 그냥 나갈래요.] -> 팝업 닫히며 메인 화면으로 이동
 * - 차단
 *     - 뒤로가기(Android) O (팝업 닫힘, 화면 제자리)
 *     - 배경 터치 O (팝업 닫힘, 화면 제자리)
 */
@Composable
fun ExitTempTimeCapsuleModal(
    onDismissRequest: () -> Unit = {},
    onExit: () -> Unit = {},
) {
    Modal(
        onDismissRequest = onDismissRequest,
        topDescription = "지금 나가면 임시 저장돼요.\n24시간 안에 다시 보관할 수 있어요.",
        topDescriptionHighlights = listOf("24시간"),
        title = "페이지를 나가시겠어요?",
        confirmLabel = "아니요, 계속할래요.",
        onConfirm = {
            // do nothing on dismiss
        },
        dismissLabel = "네, 그냥 나갈래요.",
        onDismiss = onExit,
        modalWidth = 298.dp,
    )
}
