package com.emotionstorage.ai_chat.ui.bottomSheet

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import com.emotionstorage.ai_chat.presentation.AIChatAction
import com.emotionstorage.ui.component.bottomSheet.BottomSheet

/**
 * capsule_create_02
 * - Case : 대화 종료 가능 안내
 *     - 감정 게이지가 다 찬 상태에서 [대화 종료하기]를 눌렀을 때 표출
 * - 표시 화면
 *     - 3.3 ai 감정대화
 * - CTA 및 이동
 *     - [네, 종료할래요.] -> 타임캡슐 생성 api 호출 및 capsule_create_04 로딩창 표출
 *     - [아니요, 더 이야기할래요.] -> 시트 닫히며 채팅 화면 머무름
 * - 차단
 *     - 뒤로가기(Android) O (팝업 닫힘, 화면 제자리)
 *     - 배경 터치 O (팝업 닫힘, 화면 제자리)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProposeQuitChatBottomSheet(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(),
) {
    BottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        hideDragHandle = true,
        subTitle = "감정을 충분히 이야기했어요.",
        title = "대화를 종료하고,\n지금까지의 감정을 정리해볼까요?",
        confirmLabel = "네, 종료할래요.",
        dismissLabel = "아니요, 더 이야기할래요.",
        onDismiss = {
            // do nothing
        },
        onConfirm = onConfirm,
    )
}
