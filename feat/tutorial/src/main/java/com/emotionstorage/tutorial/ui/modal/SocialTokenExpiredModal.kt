package com.emotionstorage.tutorial.ui.modal

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.emotionstorage.ui.component.modal.Modal

/**
 * onboarding_03
 * - Case : 소셜 로그인 만료
 *     - 구글/카카오 Access Token 만료
 *     - 온보딩 중 상태가 오래 유지되어, 토큰이 만료되었을 때 온보딩 마지막에서 [가입 완료하기]를 누르면 표출
 * - 표시 화면
 *     - 2.5 온보딩(4) - 약관 동의
 * - CTA 및 이동
 *     - [다시 가입하기] -> 팝업 닫히며 로그인 화면으로 이동
 * - 차단
 *     - 뒤로가기(Android) X
 *     - 배경 터치 X
 */
@Composable
fun SocialTokenExpiredModal(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
) {
    Modal(
        onDismissRequest = onDismissRequest,
        dismissOnBackPress = false,
        dismissOnClickOutside = false,
        topDescription = "소셜 로그인 인증이 만료되었어요.",
        title = "다시 시작하면\n곧 완료할 수 있어요!",
        confirmLabel = "다시 가입하기",
        onConfirm = onConfirm,
    )
}

@Preview(showBackground = true)
@Composable
private fun SocialTokenExpiredModalPreview() {
    SocialTokenExpiredModal(
        onDismissRequest = {},
        onConfirm = {},
    )
}
