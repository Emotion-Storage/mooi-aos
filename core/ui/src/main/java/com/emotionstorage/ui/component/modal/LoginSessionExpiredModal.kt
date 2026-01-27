package com.emotionstorage.ui.component.modal

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

/**
 *
 *
 * common_session_expired
 * - Case : 로그인 세션 만료(Access Token/Refresh Token 만료)
 *      - 앱이 종료되지 않은 상태에서 액세스 및 리프레시 토큰이 만료되어 로그인이 풀렸을 때 표출
 * - CTA 및 이동
 *     - [다시 로그인하기] -> 로그인 화면으로 이동
 * - 차단
 *     - 뒤로가기 X
 *     - 배경 터치 X
 */
@Composable
fun LoginSessionExpiredModal(
    onDismissRequest: () -> Unit,
    navToLogin: () -> Unit,
) {
    Modal(
        onDismissRequest = onDismissRequest,
        dismissOnBackPress = false,
        dismissOnClickOutside = false,
        title = "로그인 세션이 만료되었어요.\n다시 로그인해주세요.",
        confirmLabel = "다시 로그인하기",
        onConfirm = navToLogin,
    )
}

@Preview(showBackground = true)
@Composable
private fun LoginSessionExpiredPreview() {
    // background ui
    Box(modifier = Modifier.fillMaxSize())

    LoginSessionExpiredModal(onDismissRequest = {}, navToLogin = {})
}
