package com.emotionstorage.my.ui.modal

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.emotionstorage.ui.component.modal.Modal

/**
 * withdraw_confirm_temp
 * - Case : 회원 탈퇴 확인
 *     - [MOOI 서비스 탈퇴하기]를 눌렀을 때 표출
 * - 표시 화면
 *     - 6.7 마이페이지 - 회원탈퇴
 * - CTA 및 이동
 *     - [아니요, 그냥 있을래요.] -> 팝업 닫힘, 화면 제자리
 *     - [서비스를 탈퇴할래요.] -> 회원 탈퇴 api 호출하며 withdraw_success 팝업 표출
 * - 차단
 *     - 뒤로가기 O
 *     - 배경 터치 O
 */
// /**
// * withdraw_confirm
// * - Case : 회원 탈퇴 확인
// *      - [MOOI 서비스 탈퇴하기]를 눌렀을 때 표출
// * - 표시 화면
// *      - 8.7 마이페이지 - 회원 탈퇴
// * - CTA 및 이동
// *      - [알림을 끄고 쉬어갈래요.] -> 6.5 알림 설정 페이지로 이동
// *      - [서비스를 탈퇴할래요.] -> 회원 탈퇴 api 호출하며 withdraw_success 팝업 표출
// * - 차단
// *      - 뒤로가기(Android) O
// *      - 배경 터치 O (팝업 닫힘, 화면 제자리)
// */
@Composable
fun ConfirmWithdrawModal(
    onDismissRequest: () -> Unit,
    onWithDraw: () -> Unit,
//    onChangeNotification: () -> Unit,
) {
    Modal(
        onDismissRequest = onDismissRequest,
        title = "정말 탈퇴하시겠어요?",
        bottomDescription = "저장된 감정 기록을 다시는 볼 수 없어요",
        confirmLabel = "아니요, 그냥 있을래요.",
        onConfirm = onDismissRequest,
        dismissLabel = "서비스를 탈퇴할래요.",
        onDismiss = onWithDraw,
        dismissOnBackPress = true,
        dismissOnClickOutside = true,
    )
}

@Preview
@Composable
private fun ConfirmWithdrawModalPreview() {
    ConfirmWithdrawModal(
        onDismissRequest = {},
        onWithDraw = {},
//        onChangeNotification = {},
    )
}
