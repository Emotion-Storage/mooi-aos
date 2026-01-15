package com.emotionstorage.ai_chat.ui.modal

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emotionstorage.ui.component.modal.Modal
import com.emotionstorage.ui.theme.MooiTheme

/**
 * capsule_create_error_02
 * - Case : 타임캡슐 생성 최종 실패
 *     - capsule_create_error_01번 알럿을 총 3회 표출 후 최종 실패 처리
 * - 표시 화면
 *     - 3.3 ai 감정대화
 * - CTA 및 이동
 *     - [메인 화면으로 이동] -> 대화 임시저장 상태로 메인 화면 이동
 * - 차단
 *     - 뒤로가기(Android) X
 *     - 배경 터치 X
 */
@Composable
fun RecursiveRetryModal(navToMainScreen: () -> Unit) {
    Modal(
        onDismissRequest = {},
        dismissOnBackPress = false,
        dismissOnClickOutside = false,
        title = "계속 시도했지만\n타임캡슐 생성에 실패했어요.\n메인 화면으로 이동합니다.",
        confirmLabel = "메인 화면으로 이동",
        contentPadding = PaddingValues(top = 26.dp, start = 30.dp, end = 30.dp, bottom = 28.dp),
        onConfirm = navToMainScreen,
    )
}

@Preview
@Composable
private fun RecursiveRetryModalPreview() {
    MooiTheme {
        RecursiveRetryModal(
            navToMainScreen = {},
        )
    }
}
