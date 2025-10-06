package com.emotionstorage.time_capsule_detail.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emotionstorage.ui.component.CountDownTimer
import com.emotionstorage.ui.component.CtaButton
import com.emotionstorage.ui.theme.MooiTheme
import java.time.LocalDateTime

@Composable
fun SaveTimeCapsuleButton(
    isNewTimeCapsule: Boolean,
    modifier: Modifier = Modifier,
    expireAt: LocalDateTime? = null,
    enabled: Boolean = false,
    onSave: () -> Unit = {},
    onExpire: () -> Unit = {},
) {
    if (isNewTimeCapsule) {
        CtaButton(
            modifier = modifier.fillMaxWidth(),
            labelString = "타임캡슐 보관하기",
            enabled = enabled,
            onClick = onSave,
            isDefaultWidth = false,
        )
    } else if (expireAt != null) {
        CtaButton(
            modifier = modifier
                .fillMaxWidth()
                .height(77.dp),
            enabled = enabled,
            onClick = onSave,
            isDefaultWidth = false,
            isDefaultHeight = false,
        ) {
            CountDownTimer(
                deadline = expireAt,
            ) { hours, minutes, seconds ->
                LaunchedEffect(hours, minutes, seconds) {
                    if (hours == 0L && minutes == 0L && seconds == 0L) {
                        onExpire()
                    }
                }
                val remainingTime = String.format("%02d:%02d:%02d", hours, minutes, seconds)
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            SpanStyle(
                                color = MooiTheme.colorScheme.errorRed,
                            ),
                        ) {
                            append("${remainingTime} 남은\n")
                        }
                        append("타임캡슐 보관하기")
                    },
                    style = MooiTheme.typography.mainButton.copy(lineHeight = 20.sp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview
@Composable
private fun SaveTimeCapsuleButtonPreview() {
    MooiTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MooiTheme.colorScheme.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            SaveTimeCapsuleButton(
                isNewTimeCapsule = true,
                expireAt = LocalDateTime.now().plusHours(3),
                enabled = true,
            )

            SaveTimeCapsuleButton(
                isNewTimeCapsule = false,
                expireAt = LocalDateTime.now().plusHours(3),
                enabled = false,
            )

            SaveTimeCapsuleButton(
                isNewTimeCapsule = false,
                expireAt = LocalDateTime.now().plusHours(3),
                enabled = true,
            )
        }
    }
}
