package com.emotionstorage.auth.ui.modal

import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.emotionstorage.ui.component.modal.Modal
import com.orhanobut.logger.Logger
import androidx.core.net.toUri

/**
 * login_02
 * - 표출 조건(트리거)
 *     - Case : 로그인 처리 최종 실패
 *     login_01 알럿을 3번 표출 후 최종 실패 처리가 되었을 때 표시
 * - 표시 화면
 *     - 2.1 로그인 화면
 * - CTA 및 이동
 *     - [이메일로 문의하기] -> 팝업 닫히며 이메일 작성 인텐트 시작(화면 유지)
 *     - [닫기] -> 팝업 닫히며 이동 X(재시도 유도)
 * - 차단
 *   - 뒤로가기(Android) O (팝업 닫힘, 화면 제자리)
 *   - push/pop X
 *   - 하단바 상호작용 X (하단바 없음)
 *   - 배경 터치 O (팝업 닫힘, 화면 제자리)
 *   - 스크롤 X
 */
@Composable
fun InquireLoginErrorModal(onDismissRequest: () -> Unit) {
    val context = LocalContext.current
    Modal(
        onDismissRequest = onDismissRequest,
        topDescription = "여러 번 시도했지만\n로그인에 계속 문제가 있어요.",
        title = "잠시 후 다시 시도하거나\n메일로 문의해주세요.",
        confirmLabel = "이메일로 문의하기",
        onConfirm = {
            // todo: set email subject & title
            val emailIntent =
                Intent(Intent.ACTION_SENDTO).apply {
                    data = "mailto:".toUri()
                    putExtra(Intent.EXTRA_EMAIL, arrayOf("mooi.reply@gmail.com"))
                    putExtra(Intent.EXTRA_SUBJECT, "문의 제목")
                    putExtra(Intent.EXTRA_TEXT, "문의 내용")
                }

            try {
                context.startActivity(Intent.createChooser(emailIntent, "이메일 앱을 선택해주세요."))
            } catch (e: Exception) {
                Logger.e("이메일 앱 선택 불가능", e)
            }
            onDismissRequest()
        },
        dismissLabel = "닫기",
        onDismiss = onDismissRequest,
    )
}

@Preview(showBackground = true)
@Composable
private fun InquireLoginErrorModalPreview() {
    // background ui
    Box(modifier = Modifier.fillMaxSize())

    InquireLoginErrorModal(
        onDismissRequest = {},
    )
}
