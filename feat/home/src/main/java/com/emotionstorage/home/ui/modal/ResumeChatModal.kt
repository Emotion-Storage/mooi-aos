package com.emotionstorage.home.ui.modal

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emotionstorage.ui.component.modal.Modal
import com.emotionstorage.ui.theme.MooiTheme

/**
 * dialog_resume
 * - Case : 대화 이어하기
 *     - 동일 날짜 내에(날짜 변경되면 세션 만료) 중단된 대화가 있을 때, [대화 시작하기]를 누르면 표출
 *     - Case 1 : 사용자 의지로 dialog_leave 알럿을 통해 대화를 중단했을 경우
 *     - Case 2 : 채팅 중 앱 종료 후 앱 재실행 시 채팅 임시저장 처리되어 해당 알럿 표출
 *     - Case 3 : 채팅 중 네트워크 오류로 앱이 강제종료 된 경우
 * - 표시 화면
 *     - 3.1 메인화면
 * - CTA 및 이동
 *     - [대화를 이어서 진행할게요.] -> 팝업 닫히며 중단된 채팅 화면(3.3)으로 이동
 *     - [이전 대화를 그만할래요.] -> 팝업 닫히며 3.1화면에 머무름(중단된 대화는 종료 처리)
 * - 차단
 *     - 뒤로가기(Android) X
 *     - 배경 터치 X
 */
@Composable
fun ResumeChatModal(
    onDismissRequest: () -> Unit = {},
    onResume: () -> Unit = {},
    onDeleteChat: () -> Unit = {},
) {
    Modal(
        onDismissRequest = onDismissRequest,
        dismissOnBackPress = false,
        dismissOnClickOutside = false,
        contentPadding = PaddingValues(start = 19.dp, end = 19.dp, top = 23.dp, bottom = 28.dp),
        title = "이전 감정 대화를\n마치지 않았어요.\n이어서 계속 대화할까요?",
        bottomDescription = "'그만하기'를 누르면 이전 대화는 삭제돼요.",
        confirmLabel = "대화를 이어서 진행할게요.",
        onConfirm = onResume,
        dismissLabel = "이전 대화를 그만할래요.",
        onDismiss = onDeleteChat,
    )
}

@Preview
@Composable
private fun ResumeChatModalPreview() {
    MooiTheme {
        ResumeChatModal()
    }
}
