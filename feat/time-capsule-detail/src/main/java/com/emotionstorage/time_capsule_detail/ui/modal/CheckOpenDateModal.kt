package com.emotionstorage.time_capsule_detail.ui.modal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emotionstorage.ui.component.Modal
import com.emotionstorage.ui.theme.MooiTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@Composable
fun CheckOpenDateModal(
    createdAt: LocalDate,
    openAt: LocalDate,
    isModalOpen: Boolean = false,
    onDismissRequest: () -> Unit = {},
    onSaveOpenDate: () -> Unit = {},
) {
    if (isModalOpen) {
        Modal(
            onDismissRequest = onDismissRequest,
            contentPadding = PaddingValues(top = 23.dp, bottom = 28.dp, start = 25.dp, end = 25.dp),
            confirmLabel = "네, 보관할래요.",
            onConfirm = onSaveOpenDate,
            dismissLabel = "아니요, 다시 고를래요."
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
                                    color = MooiTheme.colorScheme.primary,
                                ),
                            ) {
                                append(createdAt.format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                            }
                            append("\n타임캡슐 오픈일: ")
                            withStyle(
                                SpanStyle(
                                    color = MooiTheme.colorScheme.primary,
                                ),
                            ) {
                                append(openAt.format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                            }
                        },
                    style = MooiTheme.typography.body1.copy(lineHeight = 24.sp),
                    color = Color.White,
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(9.dp))
                Text(
                    text = "* 임시저장 캡슐은 일일리포트에\n포함되지 않아요.",
                    style = MooiTheme.typography.body8.copy(lineHeight = 24.sp),
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
}

@Preview(showBackground = true)
@Composable
private fun CheckOpenDateModalPreview() {
    // background ui
    Box(modifier = Modifier.fillMaxSize())

    CheckOpenDateModal(
        createdAt = LocalDate.now(),
        openAt = LocalDate.now().plusDays(3),
        isModalOpen = true
    )
}
