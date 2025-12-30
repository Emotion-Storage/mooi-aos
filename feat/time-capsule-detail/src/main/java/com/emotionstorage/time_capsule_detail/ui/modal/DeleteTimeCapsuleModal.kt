package com.emotionstorage.time_capsule_detail.ui.modal

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emotionstorage.ui.component.modal.Modal

/**
 * capsule_delete
 * - Case : 타임캡슐 삭제
 *      - 타임캡슐 상세에서 [타임캡슐 삭제하기]를 눌렀을 때 표출
 * - 표시 화면
 *      - 4.4 타임캡슐 - 도착한 타임캡슐
 *      - 4.5 타임캡슐 - 임시저장 타임캡슐
 * - CTA 및 이동
 *      - [아니요, 유지할게요.] -> 팝업 닫히며 이동 X
 *      - [네, 삭제할게요.] -> 팝업 닫히며 타임캡슐 삭제 api 호출, 이전 페이지로 이동
 *  - 차단
 *      - 뒤로가기(Android) O (팝업 닫힘, 화면 제자리)
 *      - push/pop X
 *      - 하단바 상호작용 X (하단바 없음)
 *      - 배경 터치 O (팝업 닫힘, 화면 제자리)
 *      - 스크롤 X
 */
@Composable
fun DeleteTimeCapsuleModal(
    onDismissRequest: () -> Unit,
    onDelete: () -> Unit,
) {
    Modal(
        onDismissRequest = onDismissRequest,
        dismissOnClickOutside = false,
        title =
            "타임캡슐은 삭제하면\n" +
                "다시 되돌릴 수 없어요.\n" +
                "그래도 삭제할까요?",
        confirmLabel = "아니요, 유지할게요.",
        onConfirm = {
            // do nothing before dismiss
        },
        dismissLabel = "네, 삭제할게요.",
        onDismiss = onDelete,
        modalWidth = 298.dp,
    )
}

@Preview
@Composable
private fun DeleteTimeCapsuleModalPreview() {
    DeleteTimeCapsuleModal(
        onDismissRequest = {},
        onDelete = {},
    )
}
