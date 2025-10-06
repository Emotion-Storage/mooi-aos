package com.emotionstorage.time_capsule_detail.ui.component

import SpeechBubble
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.emotionstorage.domain.model.TimeCapsule
import com.emotionstorage.ui.R
import com.emotionstorage.ui.component.CountDownTimer
import com.emotionstorage.ui.component.CtaButton
import com.emotionstorage.ui.theme.MooiTheme
import java.time.LocalDateTime

@Composable
fun TimeCapsuleDetailActionButtons(
    status: TimeCapsule.Status,
    modifier: Modifier = Modifier,
    isNewTimeCapsule: Boolean = false,
    expireAt: LocalDateTime = LocalDateTime.now(),
    onSaveTimeCapsule: () -> Unit = {},
    onTimeCapsuleExpired: () -> Unit = {},
    onSaveMindNote: () -> Unit = {},
    onDeleteTimeCapsule: () -> Unit = {},
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.7.dp),
        horizontalAlignment = Alignment.End,
    ) {
        if (status == TimeCapsule.Status.TEMPORARY) {
            SaveTimeCapsuleButton(
                modifier = Modifier.padding(top = 79.dp),
                expireAt = expireAt,
                isNewTimeCapsule = isNewTimeCapsule,
                onTimeCapsuleExpired = onTimeCapsuleExpired,
                onSaveTimeCapsule = onSaveTimeCapsule,
            )
        } else {
            SaveNoteButton(
                modifier = Modifier.padding(top = 85.dp),
                onSaveNote = onSaveMindNote,
            )
        }
        // delete button
        if (!isNewTimeCapsule) {
            DeleteTimeCapsuleButton(
                onDelete = onDeleteTimeCapsule,
            )
        }
    }
}

@Composable
private fun SaveTimeCapsuleButton(
    expireAt: LocalDateTime,
    modifier: Modifier = Modifier,
    isNewTimeCapsule: Boolean = false,
    onTimeCapsuleExpired: () -> Unit = {},
    onSaveTimeCapsule: () -> Unit = {},
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (isNewTimeCapsule) {
            Text(
                modifier = Modifier.padding(bottom = 20.dp),
                text = "이 감정을 회고하고 싶다면, 타임캡슐로 보관해보세요.",
                style = MooiTheme.typography.caption7,
                color = MooiTheme.colorScheme.gray400,
            )
        } else {
            CountDownTimer(
                modifier = Modifier.padding(bottom = 15.dp),
                deadline = expireAt,
            ) { hours, minutes, seconds ->
                LaunchedEffect(hours, minutes, seconds) {
                    if (hours == 0L && minutes == 0L && seconds == 0L) {
                        onTimeCapsuleExpired()
                    }
                }

                val timerString = String.format("%02d:%02d:%02d", hours, minutes, seconds)
                SpeechBubble(
                    contentText = "이 캡슐을 보관할 수 있는 시간이\n$timerString 남았어요!",
                    tail = BubbleTail.BottomCenter,
                    sizeParam = DpSize(265.dp, 84.dp),
                    textColor = MooiTheme.colorScheme.errorRed,
                )
            }
        }
        CtaButton(
            modifier = Modifier.fillMaxWidth(),
            labelString = "타임캡슐 보관하기",
            onClick = {
                onSaveTimeCapsule()
            },
            isDefaultWidth = false,
        )
    }
}

@Composable
private fun SaveNoteButton(
    modifier: Modifier = Modifier,
    onSaveNote: () -> Unit = {},
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CtaButton(
            modifier = Modifier.fillMaxWidth(),
            labelString = "타임캡슐 저장하기",
            onClick = {
                onSaveNote()
            },
            isDefaultWidth = false,
        )
    }
}

@Composable
private fun DeleteTimeCapsuleButton(
    modifier: Modifier = Modifier,
    onDelete: () -> Unit = {},
) {
    Row(
        modifier =
            modifier
                .height(20.dp)
                .clickable(onClick = { onDelete() }),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Image(
            modifier = Modifier.size(13.dp, 14.dp),
            painter = painterResource(id = R.drawable.trash),
            contentDescription = "delete",
        )
        Text(
            text = "타임캡슐 삭제하기",
            style = MooiTheme.typography.caption6,
            color = MooiTheme.colorScheme.gray600,
        )
    }
}

@Preview
@Composable
private fun TimeCapsuleDetailActionButtonsPreview() {
    MooiTheme {
        Column(
            modifier =
                Modifier
                    .background(MooiTheme.colorScheme.background)
                    .padding(16.dp),
        ) {
            TimeCapsuleDetailActionButtons(
                status = TimeCapsule.Status.TEMPORARY,
                isNewTimeCapsule = true,
            )
            TimeCapsuleDetailActionButtons(
                expireAt = LocalDateTime.now().plusMinutes(25),
                status = TimeCapsule.Status.TEMPORARY,
            )
            TimeCapsuleDetailActionButtons(
                status = TimeCapsule.Status.LOCKED,
            )
            TimeCapsuleDetailActionButtons(
                status = TimeCapsule.Status.OPENED,
                isNewTimeCapsule = false,
            )
        }
    }
}
