package com.emotionstorage.auth.ui.modal

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.emotionstorage.ui.component.modal.Modal

/**
 * login_03
 * - Case : 가입 완료 직후 로그인 요청 실패(네트워크 오류 포함)
 *      - 가입완료 화면에서 [메인 화면으로 이동]을 눌렀을 때, 로그인 실패 시 중앙 차단 팝업 표출
 * - 표시 화면
 *      - 2.6 온보딩(5) - 가입 완료 화면
 * - CTA 및 이동
 *      - [다시 로그인하기] -> 로그인 화면으로 이동
 * - 차단
 *      - 뒤로가기(Android) X
 *      - push/pop X
 *      - 하단바 상호작용 X (하단바 없음)
 *      - 배경 터치 X
 *      - 스크롤 X
 */
@Composable
fun RetryLoginModal(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
) {
    Modal(
        onDismissRequest = onDismissRequest,
        dismissOnBackPress = false,
        dismissOnClickOutside = false,
        topDescription = "가입은 정상적으로 완료되었어요!",
        title = "하지만 로그인 연결이 잠시\n불안정해 다시 시도해야해요.",
        confirmLabel = "다시 로그인하기",
        onConfirm = onConfirm,
    )
}

@Preview
@Composable
private fun RetryLoginModalPreview() {
    RetryLoginModal(
        onDismissRequest = {},
        onConfirm = {},
    )
}
