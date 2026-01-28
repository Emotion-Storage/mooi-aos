package com.emotionstorage.time_capsule_detail.ui.modal

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emotionstorage.ui.R
import com.emotionstorage.ui.component.modal.Modal
import com.emotionstorage.ui.theme.MooiTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * capsule_save_confirm
 * - Case : 타임캡슐 보관일 확인
 *   - 임시저장 타임캡슐 보관 화면에서 [타임캡슐 보관하기]를 눌렀을 때 날짜 확인용 컨펌창 표출
 * - 표시 화면
 *   - 4.6 타임캡슐 - 임시저장 보관 설정
 * - CTA 및 이동
 *     - [네, 보관할래요.] -> 타임캡슐 저장되며 잠김 상태로 변경, 팝업 닫히며 이전 페이지로 이동
 *     - [아니요, 다시 고를래요.] -> 팝업 닫히며 이동 X
 * - 차단
 *     - 뒤로가기(Android) O (팝업 닫힘, 화면 제자리)
 *     - 배경 터치 O (팝업 닫힘, 화면 제자리)
 */
@Composable
fun CheckOpenDateModal(
    createdAt: LocalDate,
    openAt: LocalDate,
    onDismissRequest: () -> Unit = {},
    onSaveOpenDate: () -> Unit = {},
) {
    Modal(
        onDismissRequest = onDismissRequest,
        contentPadding = PaddingValues(top = 23.dp, bottom = 28.dp, start = 25.dp, end = 25.dp),
        confirmLabel = "네, 보관할래요.",
        onConfirm = onSaveOpenDate,
        dismissLabel = "아니요, 다시 고를래요.",
        topOuterContent = {
            Row(
                modifier = Modifier.padding(bottom = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Image(
                    modifier = Modifier.size(18.dp),
                    painter = painterResource(R.drawable.ic_search),
                    contentDescription = null,
                )
                Text(
                    text = "확인해주세요!",
                    style = MooiTheme.typography.body1,
                    color = Color.White,
                )
            }
        },
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text =
                    buildAnnotatedString {
                        append("감정기록일: ")
                        withStyle(
                            SpanStyle(
                                color = MooiTheme.colorScheme.primaryBlue500,
                            ),
                        ) {
                            append(createdAt.format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                        }
                        append("\n타임캡슐 오픈일: ")
                        withStyle(
                            SpanStyle(
                                color = MooiTheme.colorScheme.primaryBlue500,
                            ),
                        ) {
                            append(openAt.format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                        }
                    },
                style = MooiTheme.typography.body1,
                color = Color.White,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(9.dp))
            Text(
                text = "* 임시저장 캡슐은 일일리포트에\n포함되지 않아요.",
                style = MooiTheme.typography.body8,
                color = MooiTheme.colorScheme.gray500,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = "이 일정으로 보관할까요?",
                style = MooiTheme.typography.head2,
                color = Color.White,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CheckOpenDateModalPreview() {
    // background ui
    Box(modifier = Modifier.fillMaxSize())

    CheckOpenDateModal(
        createdAt = LocalDate.now(),
        openAt = LocalDate.now().plusDays(3),
    )
}
