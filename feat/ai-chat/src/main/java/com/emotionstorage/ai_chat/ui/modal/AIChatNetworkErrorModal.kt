package com.emotionstorage.ai_chat.ui.modal

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emotionstorage.ui.component.modal.Modal
import com.emotionstorage.ui.theme.MooiTheme

/**
 * capsule_create_error_01
 * - Case : 타임캡슐 생성 오류(네트워크 오류 포함)
 *     - api 호출 실패, 서버 오류 등 타임캡슐 생성에 실패할 경우 표시
 * - 표시 화면
 *     - 3.3 ai 감정대화
 * - CTA 및 이동
 *     - [다시 시도하기]
 *     -> 로딩 애니메이션과 함께 api 재호출, 팝업 그대로 표출
 *     -> 재실패 시, 2회 더 시도(총 3회 표출)
 *     -> 최종 실패 시, capsule_create_error_02 팝업 표출
 * - 차단
 *     - 뒤로가기(Android) X
 *     - 배경 터치 X
 */
@Composable
fun AIChatNetworkErrorModal(onRetry: () -> Unit) {
    Modal(
        onDismissRequest = {},
        dismissOnBackPress = false,
        dismissOnClickOutside = false,
        title = "감정을 정리하는 중에\n잠깐 연결이 끊긴 것 같아요.\n다시 시도해볼까요?",
        confirmLabel = "다시 시도하기",
        contentPadding = PaddingValues(top = 26.dp, start = 36.dp, end = 36.dp, bottom = 28.dp),
        onConfirm = onRetry,
    )
}

@Preview
@Composable
private fun NetworkModalPreview() {
    MooiTheme {
        AIChatNetworkErrorModal(
            onRetry = {},
        )
    }
}
