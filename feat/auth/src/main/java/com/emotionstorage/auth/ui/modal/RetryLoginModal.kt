package com.emotionstorage.auth.ui.modal

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.emotionstorage.ui.component.Modal

/**
 * login_01
 * - 표출 조건(트리거)
 *     - Case : 로그인 처리 실패
 *     로그인 요청 자체는 성공하였으나, 로그인 처리(세션/유저 정보 저장 등)에 실패한 경우 표시
 *     (서버 오류 아님)
 * - 표시 화면
 *     - 2.1 로그인 화면
 * - CTA 및 이동
 *     - [다시 로그인하기]
 *     -> 로딩 애니메이션과 함께 로그인 재시도
 *     -> 재시도 실패 시 팝업 다시 표출(2회 더, 총 3회)
 *     -> 최종 실패 시 login_02 팝업 표출
 * - 차단
 *     - 뒤로가기(Android) X
 *     - push/pop X
 *     - 하단바 상호작용 X (하단바 없음)
 *     - 배경 터치 X
 *     - 스크롤 X
 */
@Composable
fun RetryLoginModal(
    onConfirm: () -> Unit
) {
    Modal(
        onDismissRequest = {
            // disable bg click to dismiss
        },
        title = "일시적인 문제로\n로그인이 완료되지 않았어요.\n잠시 후 다시 시도해주세요.",
        bottomDescription = "네트워크가 불안정한 경우에도\n이런 현상이 발생할 수 있어요.",
        confirmLabel = "다시 로그인하기",
        onConfirm = onConfirm,
    )
}

@Preview(showBackground = true)
@Composable
private fun RetryLoginModalPreview() {
    // background ui
    Box(modifier = Modifier.fillMaxSize())

    RetryLoginModal { }
}
