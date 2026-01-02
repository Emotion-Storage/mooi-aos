package com.emotionstorage.ui.component.modal

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

/**
 * common_error_02
 * - Case : 네트워크 최상위 계층 에러
 *     - 최상위 계층까지 오류가 전파되었을 경우 표시
 *     - 즉, 본 알럿은 네트워크 오류가 발생했으나 해당 화면에서 별도의 에러 처리가 존재하지 않아 공통 에러 처리 정책이 적용되는 경우에 노출됩니다.
 *     1. 화면 전용 처리 실패 알럿 (화면별 지정된)
 *     2. 공통 네트워크 에러 알럿
 *     중에서 1번 조건에 해당하지 않는 경우 표출합니다.
 * - CTA 및 이동
 *     - [네, 확인했어요.] -> 팝업 닫히며 이동 X
 * - 차단
 *     - 뒤로가기(Android) O (팝업 닫힘, 화면 제자리)
 *     - 배경 터치 O (팝업 닫힘, 화면 제자리)
 */
@Composable
fun TempErrorModal(onDismissRequest: () -> Unit) {
    Modal(
        onDismissRequest = onDismissRequest,
        title = "일시적인 오류가 발생했어요.\n잠시 후 다시 시도해주세요.",
        confirmLabel = "네, 확인했어요.",
        onConfirm = {
            // do nothing before dismiss
        },
    )
}

@Preview(showBackground = true)
@Composable
private fun TempErrorModalPreview() {
    // background ui
    Box(modifier = Modifier.fillMaxSize())

    TempErrorModal { }
}
