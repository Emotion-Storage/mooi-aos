package com.emotionstorage.my.ui.modal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import com.emotionstorage.ui.component.modal.Modal
import kotlinx.coroutines.delay

/**
 * withdraw_success
 * - Case : 회원 탈퇴 성공
 *      - withdraw_confirm 팝업에서 [서비스를 탈퇴할래요.]를 누른 후, 성공적으로 api 호출 및 처리가 완료되었을 때 표
 * - 표시 화면
 *      - 6.7 마이페이지 - 회원탈퇴
 * - CTA 및 이동
 *      - [확인] -> 팝업 닫히며 로그인 화면으로 이동
 *      - 5초 후 Auto-dismiss -> 팝업 닫히며 로그인 화면으로 이동
 * - 차단
 *      - 뒤로가기(Android) X
 *      - push/pop X
 *      - 하단바 상호작용 X (하단바 없음)
 *      - 배경 터치 X
 *      - 스크롤 X
 */
@Composable
fun WithdrawSuccessModal(onDismissRequest: () -> Unit)  {
    // dismiss modal automatically after 5 sec
    LaunchedEffect(Unit) {
        delay(5_000)
        onDismissRequest()
    }

    Modal(
        onDismissRequest = onDismissRequest,
        dismissOnBackPress = false,
        dismissOnClickOutside = false,
        title = "회원 탈퇴가\n완료되었습니다.",
        confirmLabel = "확인",
        onConfirm = {
            // do nothing before dismiss
        },
    )
}

@Preview
@Composable
private fun WithdrawSuccessModalPreview()  {
    WithdrawSuccessModal(
        onDismissRequest = {},
    )
}
