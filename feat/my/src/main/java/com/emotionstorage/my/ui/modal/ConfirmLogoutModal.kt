package com.emotionstorage.my.ui.modal

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emotionstorage.ui.component.modal.Modal

/**
 * logout_confirm
 * - Case : 로그아웃 확인
 *      - [로그아웃] 버튼을 눌렀을 때 표출
 * - 표시 화면
 *      - 6.1 마이페이지
 * - CTA 및 이동
 *      - [아니요, 그냥 있을래요.] -> 팝업 닫히며 이동 X
 *      - [네, 로그아웃 할래요.] -> 팝업 닫히며 로그아웃 api 호출, 로그인 페이지로 이동
 * - 차단
 *      - 뒤로가기(Android) O (팝업 닫힘, 화면 제자리)
 *      - 배경 터치 O (팝업 닫힘, 화면 제자리)
 */
@Composable
fun ConfirmLogoutModal(
    onDismissRequest: () -> Unit,
    onLogout: () -> Unit,
) {
    Modal(
        onDismissRequest = onDismissRequest,
        title = "정말 로그아웃 하시겠어요?",
        contentPadding = PaddingValues(top = 23.dp, bottom = 28.dp, start = 21.dp, end = 21.dp),
        bottomDescription = "다시 돌아오실거죠? 기다리고있을게요 \uD83E\uDD7A",
        confirmLabel = "아니요, 그냥 있을래요.",
        onConfirm = {
            // do nothing before dismiss
        },
        dismissLabel = "네, 로그아웃 할래요.",
        onDismiss = onLogout,
    )
}

@Preview
@Composable
private fun ConfirmLogoutModalPreview() {
    ConfirmLogoutModal(
        onDismissRequest = {},
        onLogout = {},
    )
}
