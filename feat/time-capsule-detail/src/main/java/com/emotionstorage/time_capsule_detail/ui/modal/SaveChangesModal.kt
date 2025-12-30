package com.emotionstorage.time_capsule_detail.ui.modal

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.emotionstorage.ui.component.modal.Modal

/**
 * retrospect_unsaved
 * - Case : 나의 회고 일기 수정
 *      - 도착한 타임캡슐에서 나의 회고 일기를 쓰고(또는 수정하고), [변경사항 저장하기]를 누르지 않은 상태로 나가기(뒤로가기)를 시도했을 때 표출
 * - 표시 화면
 *      - 4.4 타임캡슐 - 도착한 타임캡슐
 * - CTA 및 이동
 *      - [네, 저장 후 나갈게요.] -> 마음노트 수정 api 호출 및 저장, 팝업 닫히며 이전 페이지로 이동
 *      - [아니요, 그냥 나갈래요.] -> 변경사항 저장 없이 팝업 닫히며 이전 페이지로 이동
 * - 차단
 *      - 뒤로가기(Android) O (팝업 닫힘, 화면 제자리)
 *      - 배경 터치 O (팝업 닫힘, 화면 제자리)
 */
@Composable
fun SaveChangesModal(
    onDismissRequest: () -> Unit,
    onSave: () -> Unit,
    onDismiss: () -> Unit,
) {
    Modal(
        onDismissRequest = onDismissRequest,
        title =
            "변경사항이 저장되지 않았어요.\n" +
                "저장 후 나갈까요?",
        confirmLabel = "네, 저장 후 나갈게요.",
        dismissLabel = "아니요, 그냥 나갈래요.",
        onConfirm = onSave,
        onDismiss = onDismiss,
    )
}

@Preview(showBackground = true)
@Composable
private fun SaveChangesModalPreview() {
    SaveChangesModal(
        onDismissRequest = {},
        onSave = {},
        onDismiss = {},
    )
}
