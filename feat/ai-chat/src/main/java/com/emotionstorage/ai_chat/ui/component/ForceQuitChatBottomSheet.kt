package com.emotionstorage.ai_chat.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emotionstorage.ui.component.bottomSheet.BottomSheet
import com.emotionstorage.ui.theme.MooiTheme

// TODO : 외부 화면을 눌렀을 때 반드시 Bottom Sheet Dismiss가 되지 않도록 해야한다
// 그냥 Modal이라 Preview 확인이 어려움
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForceQuitChatBottomSheet() {
    BottomSheet(
        onDismissRequest = { },
        hideDragHandle = true,
        subTitle = "감정 대화를 충분히 나누었어요",
        title = "지금 이 감정,\n타임캡슐에 담아둘까요?",
        confirmLabel = "타임캡슐 만들러 가기",
        contentPadding = PaddingValues(top = 23.dp, bottom = 42.dp, start = 15.dp, end = 15.dp),
        onConfirm = {},
    )
}

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
