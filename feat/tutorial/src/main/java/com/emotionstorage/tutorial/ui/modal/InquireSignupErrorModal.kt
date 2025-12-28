package com.emotionstorage.tutorial.ui.modal

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.emotionstorage.common.formatToKorDateTime
import com.emotionstorage.domain.common.ErrorCode
import com.emotionstorage.ui.component.modal.Modal
import com.emotionstorage.ui.theme.MooiTheme
import com.orhanobut.logger.Logger
import java.time.LocalDateTime

/**
 * signup_02
 * - Case : 회원가입 실패
 *     - 특정 오류 코드로 인해 회원가입을 최종 실패했을 경우 표출
 *     - (네트워크 오류 아님)
 * - 표시 화면
 *     - 2.5 온보딩(4) - 약관 동의
 * - CTA 및 이동
 *     - [이메일로 문의하기] -> 팝업 닫히며 이메일 작성 인텐트 시작(화면 유지)
 *     - [닫기] -> 팝업 닫히며 이동 X(재시도 유도)
 * - 차단
 *     - 뒤로가기(Android) O (팝업 닫힘, 화면 제자리)
 *     - push/pop X
 *     - 하단바 상호작용 X (하단바 없음)
 *     - 배경 터치 O (팝업 닫힘, 화면 제자리)
 *     - 스크롤 X
 */
@Composable
fun InquireSignupErrorModal(
    errorCode: ErrorCode,
    throwable: Throwable,
    onDismissRequest: () -> Unit,
) {
    val context = LocalContext.current
    Modal(
        onDismissRequest = onDismissRequest,
        topDescription = "회원가입에 실패했어요.",
        title = "잠시 후 다시 시도하거나\n같은 문제가 반복되면\n문의해주세요.",
        content = @Composable {
            ErrorCodeBox(errorCode)
        },
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
                        "[MOOI] 회원가입 실패 문의 (자동생성 코드: ${LocalDateTime.now()})",
                    )
                    putExtra(
                        Intent.EXTRA_TEXT,
                        """
                        ────────────────────
                        📮 문의 유형: 회원가입 실패
                        ────────────────────

                        ■ 사용하신 로그인 방식
                        ( ) 구글 로그인
                        ( ) 카카오 로그인
                        ( ) 애플 로그인
                        ( ) 기타: ________________________

                        ■ 회원가입을 시도한 시점
                        예: ${LocalDateTime.now().formatToKorDateTime("yyyy년 MM월 dd일")}경

                        ■ 온보딩 중이던 단계
                        예: 닉네임 입력 단계 / 생년월일 단계 / 약관 동의 단계 등

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

@Composable
private fun ErrorCodeBox(
    errorCode: ErrorCode,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .background(
                    MooiTheme.colorScheme.primaryBlue500.copy(alpha = 0.04f),
                    RoundedCornerShape(16.dp),
                ).border(
                    1.dp,
                    MooiTheme.colorScheme.secondaryBlue700.copy(alpha = 0.2f),
                    RoundedCornerShape(16.dp),
                ).padding(vertical = 16.dp, horizontal = 8.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "오류 코드: $errorCode",
            modifier = Modifier.align(Alignment.Center),
            style = MooiTheme.typography.body5,
            color = MooiTheme.colorScheme.gray500,
        )
    }
}

@Preview
@Composable
private fun InquireSignupErrorModalPreview() {
    InquireSignupErrorModal(
        errorCode = ErrorCode.UNKNOWN,
        throwable = Throwable("message"),
        onDismissRequest = {},
    )
}
