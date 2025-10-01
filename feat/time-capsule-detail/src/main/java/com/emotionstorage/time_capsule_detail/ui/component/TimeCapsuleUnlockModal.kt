package com.emotionstorage.time_capsule_detail.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.emotionstorage.common.getDaysBetween
import com.emotionstorage.ui.R
import com.emotionstorage.ui.component.CountDownTimer
import com.emotionstorage.ui.component.CtaButton
import com.emotionstorage.ui.theme.MooiTheme
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.math.absoluteValue

@Composable
fun TimeCapsuleUnlockModal(
    keyCount: Int,
    requiredKeyCount: Int,
    arriveAt: LocalDateTime,
    isModalOpen: Boolean = false,
    onDismissRequest: () -> Unit = {},
    onUnlock: () -> Unit = {},
) {
    val canUnlock = keyCount >= requiredKeyCount

    if (isModalOpen) {
        // custom modal
        Dialog(onDismissRequest = {
            // cannot dismiss unless confirm button clicked
        }) {
            Column(
                modifier =
                    Modifier
                        .clip(RoundedCornerShape(15.dp))
                        .background(MooiTheme.colorScheme.background)
                        // todo: align center
                        .padding(top = 19.dp, bottom = 29.dp, start = 30.dp, end = 24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // icon
                Image(
                    modifier = Modifier.size(17.dp, 20.dp),
                    painter = painterResource(id = R.drawable.lock),
                    contentDescription = "lock",
                )
                // title & descriptions
                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 13.dp, bottom = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        modifier = Modifier.padding(bottom = 8.dp),
                        text =
                            buildAnnotatedString {
                                append("이 타임캡슐을 미리 열려면\n")
                                withStyle(
                                    SpanStyle(
                                        color = MooiTheme.colorScheme.primary,
                                    ),
                                ) {
                                    append("열쇠 3개")
                                }
                                append("가 필요해요!")
                            },
                        style = MooiTheme.typography.body5.copy(lineHeight = 24.sp),
                        color = MooiTheme.colorScheme.gray500,
                        textAlign = TextAlign.Center,
                    )
                    if (arriveAt.toLocalDate() == LocalDate.now()) {
                        // run countdown timer, if arriveAt is within an day
                        CountDownTimer(
                            deadline = arriveAt,
                            optimizeMinuteTick = true,
                            optimizeSecondTick = true,
                        ) { hours, minutes, seconds ->
                            LaunchedEffect(hours, minutes, seconds) {
                                if (hours == 0L && minutes == 0L && seconds == 0L) {
                                    // close modal if count down time is zero
                                    onDismissRequest()
                                }
                            }

                            Text(
                                modifier = Modifier.height(30.dp),
                                text =
                                    "남은 기간 : " +
                                        (if (hours == 0L) "" else "${hours}시간 ") +
                                        (if (hours != 0L && minutes == 0L) "" else "${minutes}분"),
                                style = MooiTheme.typography.head2,
                                color = Color.White,
                            )
                        }
                    } else {
                        Text(
                            modifier = Modifier.height(30.dp),
                            text = "남은 기간 : ${
                                LocalDate.now().getDaysBetween(arriveAt.toLocalDate()).absoluteValue
                            }일",
                            style = MooiTheme.typography.head2,
                            color = Color.White,
                        )
                    }
                    Text(
                        modifier = Modifier.height(30.dp),
                        text = "현재 보유 열쇠 : ${keyCount}개",
                        style = MooiTheme.typography.head2,
                        color = Color.White,
                    )
                }
                // button
                CtaButton(
                    modifier =
                        Modifier
                            .height(
                                if (canUnlock) 50.dp else 65.dp,
                            ).fillMaxWidth(),
                    enabled = canUnlock,
                    onClick = {
                        onUnlock()
                        onDismissRequest()
                    },
                    radius = 10,
                    isDefaultHeight = false,
                    isDefaultWidth = false,
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = "열쇠 ${requiredKeyCount}개 사용해서 열기",
                            style = MooiTheme.typography.mainButton,
                        )
                        if (!canUnlock) {
                            Text(
                                text = "(보유 열쇠 부족)",
                                style = MooiTheme.typography.caption3,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TimeCapsuleUnlockModalPreview() {
    // background ui
    Box(modifier = Modifier.fillMaxSize())

    TimeCapsuleUnlockModal(
        keyCount = 1,
        requiredKeyCount = 3,
        arriveAt = LocalDateTime.now().plusMinutes(1),
        isModalOpen = true,
        onDismissRequest = { },
    )
}
