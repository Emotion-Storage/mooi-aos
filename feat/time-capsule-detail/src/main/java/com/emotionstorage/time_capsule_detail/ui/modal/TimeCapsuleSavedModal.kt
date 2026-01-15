package com.emotionstorage.time_capsule_detail.ui.modal

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emotionstorage.ui.R
import com.emotionstorage.ui.component.modal.Modal
import com.emotionstorage.ui.theme.MooiTheme
import com.emotionstorage.ui.util.buildHighlightAnnotatedString
import kotlinx.coroutines.delay

/**
 * capsule_save_success
 * - Case : 타임캡슐 보관 성공
 *     - 오픈일 설정 후 [타임캡슐 보관하기]를 눌렀을 때 표출
 * - 표시 화면
 *     - 3.6 타임캡슐 - 보관 기간 설정
 *     - 4.6 타임캡슐 - 임시저장 보관 설정
 * - CTA 및 이동
 *     - [메인 화면으로 이동] -> 팝업 닫히며 메인 화면으로 이동
 * - 차단
 *     - 뒤로가기(Android) X
 *     - 배경 터치 X
 */
@Composable
fun TimeCapsuleSavedModal(
    isModalOpen: Boolean = false,
    onConfirm: () -> Unit = {},
) {
    LaunchedEffect(isModalOpen) {
        if (isModalOpen) {
            // confirm automatically after 5 seconds
            delay(5000)
            onConfirm()
        }
    }

    if (isModalOpen) {
        Modal(
            onDismissRequest = {},
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            confirmLabel = "네, 확인했어요.",
            onConfirm = onConfirm,
            contentPadding = PaddingValues(top = 9.dp, bottom = 28.dp, start = 27.dp, end = 27.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    modifier = Modifier.size(82.dp, 67.dp),
                    painter = painterResource(id = R.drawable.graphic_ai_chat),
                    contentDescription = null

                )
                Text(
                    modifier = Modifier.padding(top = 4.dp, bottom = 6.dp),
                    text = "타임캡슐을\n안전하게 보관했어요.",
                    style =
                        MooiTheme.typography.head2.copy(
                            lineHeight = 30.sp,
                        ),
                    color = Color.White,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = "이 감정은 추후 더\n소중한 이야기가 될 거예요.",
                    style = MooiTheme.typography.body5,
                    color = MooiTheme.colorScheme.gray500,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TimeCapsuleSavedModalPreview() {
    // background ui
    Box(modifier = Modifier.fillMaxSize())

    TimeCapsuleSavedModal(
        isModalOpen = true,
    )
}
