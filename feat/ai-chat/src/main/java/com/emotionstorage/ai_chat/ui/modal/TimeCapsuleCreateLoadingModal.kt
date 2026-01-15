package com.emotionstorage.ai_chat.ui.modal

import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emotionstorage.ui.component.modal.Modal
import com.emotionstorage.ui.theme.MooiTheme
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.emotionstorage.ui.R

/**
 * capsule_create_04
 * - Case : 타임캡슐 생성 로딩
 *     - 타임캡슐 생성 api 트리거 후 즉시 표시
 *     - api 요청 중 표시함
 * - 표시 화면
 *     - 3.3 ai 감정대화
 * - CTA 및 이동
 *     - API 응답 성공 -> 로딩 모달 즉시 닫고 다음 화면으로 이동
 *     - API 실패 -> 로딩 모달 닫고 실패 알럿(capsule_create_error_01) 표시
 * - 차단
 *     - 뒤로가기(Android) X
 *     - 배경 터치 X
 */
@Composable
fun TimeCapsuleCreateLoadingModal() {
    val context = LocalContext.current

    Modal(
        onDismissRequest = { },
        dismissOnBackPress = false,
        dismissOnClickOutside = false,
        topDescription = "잠시만 기다려주세요.",
        title = "지금 나눈 감정을\n타임캡슐에 담는 중이에요.",
        verticalSpacing = 0.dp,
        contentPadding = PaddingValues(top = 23.dp, bottom = 8.dp, start = 36.dp, end = 36.dp),
        content = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    modifier = Modifier.size(74.dp),
                    painter =
                        rememberDrawablePainter(
                            drawable =
                                getDrawable(
                                    context,
                                    R.drawable.gif_loading,
                                ),
                        ),
                    contentDescription = "animated gif",
                )
            }
        },
    )
}

@Preview
@Composable
private fun TimeCapsuleCreateLoadingModalPreview() {
    MooiTheme {
        TimeCapsuleCreateLoadingModal()
    }
}
