package com.emotionstorage.time_capsule_detail.ui.modal

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emotionstorage.ui.component.modal.Modal

/**
 * capsule_expired
 * - Case : 임시저장 시간 초과
 *      - 임시저장 타임캡슐 보관 가능 시간이 초과되었을 때 페이지가 자동 리다이렉트되며 표출
 *      - (4.1 바텀시트 화면에서는 목록을 클릭했을 때 표출)
 * - 표시 화면
 *      - 4.1 타임캡슐 - 달력형 보기
 *      - 4.5 타임캡슐 - 임시저장 타임캡슐
 *      - 4.6 타임캡슐 - 임시저장 보관 설정
 * - CTA 및 이동
 *      - [네, 확인했어요.] -> 팝업 닫히며 타임캡슐 삭제 api 호출, 이전 페이지로 이동
 *      - (4.1 바텀시트 화면에서는 리스트만 사라지며 화면 이동 X)
 * - 차단
 *      - 뒤로가기 X,
 *      - 배경 터치 X
 */
@Composable
fun TimeCapsuleExpiredModal(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    ) {
    Modal(
        onDismissRequest = onDismissRequest,
        dismissOnClickOutside = false,
        dismissOnBackPress = false,
        title =
            "보관 기한이 만료되어\n캡슐을 보관할 수 없어요.",
        bottomDescription = "새로운 감정은 새 타임캡슐에 담아보세요.",
        confirmLabel = "네, 확인했어요.",
        onConfirm = onConfirm,
        modalWidth = 298.dp
    )
}

@Preview(showBackground = true)
@Composable
private fun TimeCapsuleExpiredModalPreview() {
    TimeCapsuleExpiredModal(
        onDismissRequest = {},
        onConfirm = {},
    )
}
