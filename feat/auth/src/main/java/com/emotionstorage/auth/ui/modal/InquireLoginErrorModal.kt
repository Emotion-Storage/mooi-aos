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
import com.emotionstorage.common.formatToKorDateTime
import com.emotionstorage.domain.common.ErrorCode
import java.time.LocalDateTime

/**
 * login_02
 * - Case : 로그인 처리 최종 실패
 *     - login_01 알럿을 3번 표출 후 최종 실패 처리가 되었을 때 표시
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
fun InquireLoginErrorModal(
    errorCode: ErrorCode,
    throwable: Throwable,
    onDismissRequest: () -> Unit,
) {
    val context = LocalContext.current
    Modal(
        onDismissRequest = onDismissRequest,
        topDescription = "여러 번 시도했지만\n로그인에 계속 문제가 있어요.",
        title = "잠시 후 다시 시도하거나\n메일로 문의해주세요.",
        confirmLabel = "이메일로 문의하기",
        onConfirm = {
            val emailIntent =
                Intent(Intent.ACTION_SENDTO).apply {
                    data = "mailto:".toUri()
                    putExtra(
                        Intent.EXTRA_EMAIL,
                        arrayOf("mooi.reply@gmail.com"),
                    )
                    putExtra(
                        Intent.EXTRA_SUBJECT,
                        "[MOOI] 로그인 실패 문의 (자동생성 코드: ${LocalDateTime.now()})",
                    )
                    putExtra(
                        Intent.EXTRA_TEXT,
                        """
                        ────────────────────
                        📮 문의 유형: 로그인 실패
                        ────────────────────

                        ■ 사용하신 로그인 방식
                        ( ) 구글 로그인
                        ( ) 카카오 로그인
                        ( ) 애플 로그인
                        ( ) 기타: ________________________

                        ■ 로그인 시도 시점
                        예: ${LocalDateTime.now().formatToKorDateTime("yyyy년 MM월 dd일")}경

                        ■ 오류 발생 화면
                        예: 회원가입 완료 단계 / 로그인 버튼 클릭 직후

                        ■ 사용 기기 및 OS
                        예: Galaxy S22 / Android 14

                        ■ 앱 버전
                        예: 1.0.0 (설정 > 앱 정보에서 확인 가능)

                        ■ 네트워크 환경
                        ( ) Wi-Fi
                        ( ) 5G/LTE
                        ( ) 기타: ________________________

                        ■ 추가로 알려주실 내용이 있다면 자유롭게 적어주세요


                        📎 자동 포함 정보
                        - 오류 코드: $errorCode
                        - 오류 메세지: ${throwable.message}
                        - 오류 원인: ${throwable.cause}
                        """.trimIndent(),
                    )
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
        errorCode = ErrorCode.UNKNOWN,
        throwable = Throwable("message"),
        onDismissRequest = {},
    )
}
