package com.emotionstorage.time_capsule_detail.ui.modal

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.sp
import com.emotionstorage.common.getDaysBetween
import com.emotionstorage.ui.R
import com.emotionstorage.ui.component.CountDownTimer
import com.emotionstorage.ui.component.modal.Modal
import com.emotionstorage.ui.theme.MooiTheme
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.math.absoluteValue

/**
 * capsule_open_01 & capsule_open_02
 * - Case : 타임캡슐 열기 시도
 *      - 잠김 상태(도착일이 도래하지 않은)의 타임캡슐 목록을 클릭했을 때 상세페이지에서 표시
 * - 표시 화면
 *      - 4.4 타임캡슐 - 도착한 타임캡슐
 * - CTA 및 이동 1
 *      - [열쇠 n개 사용하기] -> 열쇠 차감되며 타임캡슐 오픈(열림 상태로 변경)
 *      - [지금은 열지 않을래요.] -> 팝업 닫히며 이전 페이지로 이동
 * - CTA 및 이동 2
 *      - [뒤로가기] -> 팝업 닫히며 이전 페이지로 이동
 * - 차단
 *      - 뒤로가기(Android) O
 *      - 배경 터치 X
 */
@Composable
fun UnlockTimeCapsuleModal(
    onDismissRequest: () -> Unit,
    keyCount: Int,
    requiredKeyCount: Int,
    openAt: LocalDateTime,
    onConfirm: () -> Unit = {},
) {
    val canUnlock = keyCount >= requiredKeyCount

    Modal(
        onDismissRequest = onDismissRequest,
        dismissOnClickOutside = false,
        confirmLabel = if (canUnlock) "열쇠 ${requiredKeyCount}개 사용하기" else null,
        onConfirm = onConfirm,
        dismissLabel = if (canUnlock) "지금은 열지 않을래요." else "뒤로 가기",
        onDismiss = {
            // do nothing before dismiss
        },
        modalWidth = 298.dp,
        contentPadding = PaddingValues(top = 16.dp, bottom = 28.dp),
        verticalSpacing = if (canUnlock) 18.dp else 12.dp
    ) {
        ModalContent(
            keyCount = keyCount,
            requiredKeyCount = requiredKeyCount,
            openAt = openAt,
        )
    }
}

@Composable
private fun ModalContent(
    keyCount: Int,
    requiredKeyCount: Int,
    openAt: LocalDateTime,
    modifier: Modifier = Modifier,
) {
    val canUnlock = keyCount >= requiredKeyCount

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // icon
        Image(
            modifier = Modifier.size(17.dp, 25.dp),
            painter = painterResource(id = R.drawable.ic_lock),
            contentDescription = "lock",
        )
        // title & descriptions
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                modifier = Modifier.padding(bottom = 8.dp),
                text =
                    buildAnnotatedString {
                        append("이 타임캡슐을 미리 열려면\n")
                        withStyle(
                            SpanStyle(
                                color = MooiTheme.colorScheme.primaryBlue500,
                            ),
                        ) {
                            append("열쇠 ${requiredKeyCount}개")
                        }
                        append("가 필요해요!")
                    },
                style = MooiTheme.typography.body5,
                color = MooiTheme.colorScheme.gray500,
                textAlign = TextAlign.Center,
            )

            if (openAt.toLocalDate() == LocalDate.now()) {
                // run countdown timer, if openAt is within an day
                CountDownTimer(
                    deadline = openAt,
                    optimizeMinuteTick = true,
                    optimizeSecondTick = true,
                ) { hours, minutes, seconds ->
                    Text(
                        modifier = Modifier.height(30.dp),
                        text =
                            "남은 기간 : " +
                                (if (hours == 0L) "" else "${hours}시간 ") +
                                (if (hours != 0L && minutes == 0L) "" else "${minutes}분"),
                        style = MooiTheme.typography.head2.copy(lineHeight = 30.sp),
                        color = Color.White,
                    )
                }
            } else {
                Text(
                    modifier = Modifier.height(30.dp),
                    text = "남은 기간 : ${
                        LocalDate.now().getDaysBetween(openAt.toLocalDate()).absoluteValue
                    }일",
                    style = MooiTheme.typography.head2.copy(lineHeight = 30.sp),
                    color = Color.White,
                )
            }

            Text(
                modifier = Modifier.height(30.dp),
                text =
                    buildAnnotatedString {
                        append("현재 보유 열쇠 : ")
                        withStyle(
                            SpanStyle(
                                color = if (canUnlock) Color.White else MooiTheme.colorScheme.error,
                            ),
                        ) {
                            append("${keyCount}개")
                        }
                    },
                style = MooiTheme.typography.head2,
                color = Color.White,
            )

            if (!canUnlock) {
                Row(
                    modifier = Modifier.padding(top = 18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(7.dp),
                ) {
                    Icon(
                        modifier =
                            Modifier
                                .size(18.dp),
                        painter = painterResource(id = R.drawable.ic_caution),
                        contentDescription = "경고 아이콘",
                        tint = MooiTheme.colorScheme.error,
                    )
                    Text(
                        text = "지금 사용할 수 있는 열쇠가 부족해요.",
                        color = MooiTheme.colorScheme.error,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun UnlockTimeCapsuleModalContentPreview() {
    ModalContent(
        modifier = Modifier
            .background(MooiTheme.colorScheme.backgroundDefault)
            .width(298.dp)
            .padding(start = 27.dp, end = 27.dp, top = 16.dp, bottom = 18.dp),
        keyCount = 3,
        requiredKeyCount = 1,
        openAt = LocalDateTime.now().plusMinutes(30),
    )
}

@Preview
@Composable
private fun UnlockTimeCapsuleModalContentPreview2() {
    ModalContent(
        modifier = Modifier
            .background(MooiTheme.colorScheme.backgroundDefault)
            .width(298.dp)
            .padding(start = 27.dp, end = 27.dp, top = 16.dp, bottom = 12.dp),
        keyCount = 1,
        requiredKeyCount = 3,
        openAt = LocalDateTime.now().plusMinutes(30),
    )
}
