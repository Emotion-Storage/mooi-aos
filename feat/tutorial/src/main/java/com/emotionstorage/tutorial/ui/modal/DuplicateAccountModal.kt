package com.emotionstorage.tutorial.ui.modal

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.emotionstorage.ui.component.modal.Modal

/**
 * signup_01
 * - Case : 중복 가입
 *     - 동시에 타 디바이스에서 똑같은 소셜 id로 가입을 시도할 때, 먼저 가입한 기기 및 ID가 존재하는 경우
 *     - 온보딩 마지막에서 [가입 완료하기]를 누르면 ID 유무 체크하며 알럿 표출
 * - 표시 화면
 *     - 2.5 온보딩(4) - 약관 동의
 * - CTA 및 이동
 *     - [다시 로그인하기] -> 팝업 닫히며 로그인 화면으로 이동
 * - 차단
 *     - 뒤로가기(Android) X
 *     - 배경 터치 X
 */
@Composable
fun DuplicateAccountModal(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
) {
    Modal(
        onDismissRequest = onDismissRequest,
        dismissOnBackPress = false,
        dismissOnClickOutside = false,
        topDescription = "이미 가입된 계정이에요!",
        title = "로그인하면\n바로 이용할 수 있어요.",
        confirmLabel = "다시 로그인하기",
        onConfirm = onConfirm,
    )
}

@Preview
@Composable
private fun DuplicateAccountModalPreview() {
    DuplicateAccountModal(
        onDismissRequest = {},
        onConfirm = {},
    )
}
