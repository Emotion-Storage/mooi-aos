package com.emotionstorage.my.ui.modal

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.emotionstorage.ui.component.modal.Modal

/**
 * withdraw_confirm
 * - Case : 회원 탈퇴 확인
 *      - [MOOI 서비스 탈퇴하기]를 눌렀을 때 표출
 * - 표시 화면
 *      - 8.7 마이페이지 - 회원 탈퇴
 * - CTA 및 이동
 *      - [알림을 끄고 쉬어갈래요.] -> 6.5 알림 설정 페이지로 이동
 *      - [서비스를 탈퇴할래요.] -> 회원 탈퇴 api 호출하며 withdraw_success 팝업 표출
 * - 차단
 *      - 뒤로가기(Android) O
 *      - push/pop X
 *      - 하단바 상호작용 X (하단바 없음)
 *      - 배경 터치 O (팝업 닫힘, 화면 제자리)
 *      - 스크롤 X
 */
@Composable
fun ConfirmWithdrawModal(
    onDismissRequest: () -> Unit,
    onChangeNotification: () -> Unit,
    onWithDraw: () -> Unit
) {
    Modal(
        onDismissRequest = onDismissRequest,
        title = "기록을 잠시 멈추고 싶다면,\n알림을 끄거나\n앱을 쉬어가보는 건 어떨까요?",
        confirmLabel = "알림을 끄고 쉬어갈래요.",
        onConfirm = onChangeNotification,
        dismissLabel = "서비스를 탈퇴할래요.",
        onDismiss = onWithDraw,
    )
}

@Preview
@Composable
private fun ConfirmWithdrawModalPreview(){
    ConfirmWithdrawModal(
        onDismissRequest = {},
        onChangeNotification = {},
        onWithDraw = {},
    )
}
