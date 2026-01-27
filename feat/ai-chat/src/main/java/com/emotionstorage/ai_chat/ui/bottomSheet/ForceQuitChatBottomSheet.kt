package com.emotionstorage.ai_chat.ui.bottomSheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emotionstorage.ui.component.bottomSheet.BottomSheet
import com.emotionstorage.ui.theme.MooiTheme

/**
 * capsule_create_03
 * - Case : 대화 강제 종료
 *     - 감정 게이지가 다 찼음에도 불구하고 대화를 종료하지 않고 10턴을 초과했을 때 표시
 * - 표시 화면
 *     - 3.3 ai 감정대화
 * - CTA 및 이동
 *     - [타임캡슐 만들러 가기] -> 타임캡슐 생성 api 호출 및 capsule_create_04 로딩창 표출
 * - 차단
 *     - 뒤로가기(Android) X
 *     - 배경 터치 X
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForceQuitChatBottomSheet(onConfirm: () -> Unit = {}) {
    BottomSheet(
        sheetState =
            rememberModalBottomSheetState(
                skipPartiallyExpanded = true,
                confirmValueChange = { next ->
                    next != SheetValue.Hidden
                },
            ),
        onDismissRequest = {
            // no operation
        },
        hideDragHandle = true,
        shouldDismissOnBackPress = false,
        forbidDismiss = true,
        subTitle = "감정 대화를 충분히 나누었어요",
        title = "지금 이 감정,\n타임캡슐에 담아둘까요?",
        confirmLabel = "타임캡슐 만들러 가기",
        contentPadding = PaddingValues(top = 23.dp, bottom = 42.dp, start = 15.dp, end = 15.dp),
        onConfirm = onConfirm,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun ForceQuitChatBottomSheetPreview() {
    MooiTheme {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(Color.White),
        ) {
            ForceQuitChatBottomSheet()
        }
    }
}
