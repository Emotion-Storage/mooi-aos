package com.emotionstorage.my.ui.modal

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
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.emotionstorage.common.formatToKorDateTime
import com.emotionstorage.domain.common.ErrorCode
import com.emotionstorage.ui.component.modal.Modal
import com.emotionstorage.ui.theme.MooiTheme
import com.orhanobut.logger.Logger
import java.time.LocalDateTime

/**
 * withdraw_failed
 * - Case : 회원 탈퇴 실패
 *      - withdraw_confirm 팝업에서 [서비스를 탈퇴할래요.]를 누른 후, api 호출 및 처리에 실패했을 때 표출
 *      - (네트워크 오류 아님)
 * - 표시 화면
 *      - 6.7 마이페이지 - 회원탈퇴
 * - CTA 및 이동
 *      - [이메일로 문의하기] -> 팝업 닫히며 이메일 작성 인텐트 시작(화면 유지)
 *      - [닫기] -> 팝업 닫히며 이동 X(재시도 유도)
 * - 차단
 *      - 뒤로가기(Android) O (팝업 닫힘, 화면 제자리)
 *      - push/pop X
 *      - 하단바 상호작용 X (하단바 없음)
 *      - 배경 터치 O (팝업 닫힘, 화면 제자리)
 *      - 스크롤 X
 */
@Composable
fun InquireWithdrawErrorModal(
    errorCode: ErrorCode,
    throwable: Throwable,
    onDismissRequest: () -> Unit,
    userEmail: String?,
    userNickname: String?,
) {
    val context = LocalContext.current
    Modal(
        onDismissRequest = onDismissRequest,
        topDescription = "회원탈퇴에 실패했어요.",
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
                        "[MOOI] 회원탈퇴 요청 (자동생성 코드: ${LocalDateTime.now()})",
                    )
                    putExtra(
                        Intent.EXTRA_TEXT,
                        """
                        ────────────────────
                        📮 문의 유형: 회원탈퇴 요청
                        ────────────────────

                        ■ 탈퇴를 원하는 계정의 로그인 방식
                        ( ) 구글
                        ( ) 카카오
                        ( ) 애플
                        ( ) 기타: ________________________

                        ■ 계정에 등록된 이메일과 닉네임
                        예: ${userEmail ?: "hello_user@naver.com"} / 닉네임 ‘${userNickname ?: "moodlover"}’

                        ■ 탈퇴를 요청하시는 이유(선택)
                        ( ) 더 이상 앱을 사용하지 않아요
                        ( ) 개인 정보 삭제를 원해요
                        ( ) 이용 중 불편함이 있었어요
                        ( ) 기타: ________________________________________

                        ■ 탈퇴를 요청하신 시점
                        예: ${LocalDateTime.now().formatToKorDateTime("yyyy년 MM월 dd일")}경

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
        },
        dismissLabel = "닫기",
        onDismiss = {
            // do nothing before dismiss
        },
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
                )
                .border(
                    1.dp,
                    MooiTheme.colorScheme.secondaryBlue700.copy(alpha = 0.2f),
                    RoundedCornerShape(16.dp),
                )
                .padding(vertical = 16.dp, horizontal = 8.dp),
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
