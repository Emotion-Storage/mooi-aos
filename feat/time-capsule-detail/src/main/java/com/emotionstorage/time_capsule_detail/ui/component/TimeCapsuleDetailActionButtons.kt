package com.emotionstorage.time_capsule_detail.ui.component

import SpeechBubble
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
    expireAt: LocalDateTime,
    status: TimeCapsule.STATUS,
    modifier: Modifier = Modifier,
    isNewTimeCapsule: Boolean = false,
    onSaveTimeCapsule: () -> Unit = {},
    onTimeCapsuleExpired: () -> Unit = {},
    onSaveMindNote: () -> Unit = {},
    onDeleteTimeCapsule: () -> Unit = {},
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.7.dp),
        horizontalAlignment = Alignment.End,
    ) {
        if (status == TimeCapsule.STATUS.TEMPORARY) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 79.dp),
                verticalArrangement = Arrangement.spacedBy(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                if (isNewTimeCapsule) {
                    Text(
                        modifier = Modifier.padding(bottom = 5.dp),
                        text = "이 감정을 회고하고 싶다면, 타임캡슐로 보관해보세요.",
                        style = MooiTheme.typography.caption7,
                        color = MooiTheme.colorScheme.gray400,
                    )
                } else {
                    CountDownTimer(
                        deadline = expireAt,
                    ) { hours, minutes, seconds ->
                        LaunchedEffect(hours, minutes, seconds) {
                            if (hours == 0L && minutes == 0L && seconds == 0L) {
                                onTimeCapsuleExpired()
                            }
                        }

                        val timerString = String.format("%02d:%02d:%02d", hours, minutes, seconds)
                        SpeechBubble(
                            text = "이 캡슐을 보관할 수 있는 시간이\n$timerString 남았어요!",
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
        } else {
            // todo: change action button according to status
            CtaButton(
                modifier = Modifier.fillMaxWidth(),
                labelString = "타임캡슐 저장하기",
                isDefaultWidth = false,
            )
        }

        // delete button
        if (!isNewTimeCapsule) {
            Row(
                modifier =
                    Modifier
                        .height(20.dp)
                        .clickable(onClick = { onDeleteTimeCapsule() }),
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
    }
}

@Composable
private fun DeleteTimeCapsuleButton(
    modifier: Modifier = Modifier,
    onDelete: () -> Unit = {}
){
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
