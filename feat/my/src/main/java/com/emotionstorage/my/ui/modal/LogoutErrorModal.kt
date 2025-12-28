package com.emotionstorage.my.ui.modal

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.emotionstorage.ui.component.modal.Modal

/**
 * logout_failed
 * - Case : 로그아웃 실패(네트워크 오류 포함)
 *      - 로그아웃 api 호출 및 처리 실패 시 표시
 * - 표시 화면
 *      - 6.1 마이페이지
 * - CTA 및 이동
 *      - [다시 시도하기] -> 로딩 애니메이션 띄우며 로그아웃 api 재호출
 * - 차단
 *      - 뒤로가기(Android) O (팝업 닫힘, 화면 제자리)
 *      - push/pop X
 *      - 하단바 상호작용 X (하단바 없음)
 *      - 배경 터치 O (팝업 닫힘, 화면 제자리)
 *      - 스크롤 X
 */
@Composable
fun LogoutErrorModal(
    onDismissRequest: () -> Unit,
    onRetry: () -> Unit,
) {
    Modal(
        onDismissRequest = onDismissRequest,
        topDescription = "로그아웃에 실패했어요.",
        title = "연결 상태를 확인한 뒤\n다시 시도해주세요.",
        confirmLabel = "다시 로그아웃하기",
        onConfirm = onRetry,
    )
}


@Preview
@Composable
private fun LogoutErrorModalPreview(){
    LogoutErrorModal(
        onDismissRequest = {},
        onRetry = {},
    )
}
