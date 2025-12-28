package com.emotionstorage.auth.ui.modal

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.emotionstorage.ui.component.modal.Modal

// todo: 알럿 정의서에 따라 코멘트 내용 수정하기

/**
 * 로그인 재시도 팝업
 * 회원가입 완료 화면에서, 회원가입 성공 but 로그인 실패한 경우 표시
 * [다시 로그인하기] -> 로그인 화면으로 이동
 * 배경 클릭 X, 뒤로가기 X
 */
@Composable
fun RetryLoginModal(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit) {
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
