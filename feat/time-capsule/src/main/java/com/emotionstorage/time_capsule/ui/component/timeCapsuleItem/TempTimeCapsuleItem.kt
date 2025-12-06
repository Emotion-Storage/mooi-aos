package com.emotionstorage.time_capsule.ui.component.timeCapsuleItem

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.emotionstorage.time_capsule.ui.model.TimeCapsuleItemState
import com.emotionstorage.ui.R
import com.emotionstorage.ui.component.CountDownTimer
import com.emotionstorage.ui.theme.MooiTheme
import com.emotionstorage.ui.util.errorRedBackground

@Composable
fun TempTimeCapsuleItem(
    timeCapsule: TimeCapsuleItemState,
    modifier: Modifier = Modifier,
    showHeader: Boolean = true,
    onClick: () -> Unit = {},
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .background(Color.Transparent),
    ) {
        // header
        if (showHeader && timeCapsule.expireAt != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(7.dp),
            ) {
                Image(
                    modifier = Modifier.size(16.dp),
                    painter = painterResource(R.drawable.ic_caution),
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(MooiTheme.colorScheme.errorRed),
                )
                CountDownTimer(
                    deadline = timeCapsule.expireAt,
                    optimizeMinuteTick = true,
                    optimizeSecondTick = true,
                ) { hours, minutes, _ ->
                    Text(
                        text =
                            "임시저장 보관기간이 " +
                                (if (hours >= 1) "${hours}시간 " else "${minutes}분 ") +
                                "남았어요.",
                        style = MooiTheme.typography.caption6,
                        color = MooiTheme.colorScheme.errorRed,
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(7.dp))
    }
    // content
    TempContent(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
    )
}

@Composable
private fun TempContent(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Row(
        modifier =
            modifier
                .height(90.dp)
                .fillMaxWidth()
                .errorRedBackground(
                    true,
                    RoundedCornerShape(15.dp),
                ).clip(RoundedCornerShape(15.dp))
                .clickable(onClick = onClick)
                .padding(start = 15.dp, end = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(
            modifier =
                Modifier
                    .padding(bottom = 6.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text(
                modifier = Modifier.height(24.dp),
                text = "아직 보관하지 않은 타임캡슐이 있어요.",
                style = MooiTheme.typography.caption3,
                color = MooiTheme.colorScheme.gray500,
                textAlign = TextAlign.Center,
            )
            Text(
                modifier = Modifier.height(24.dp),
                text = "이어서 보관하러 갈까요?",
                style = MooiTheme.typography.body1,
                color = Color.White,
                textAlign = TextAlign.Center,
            )
        }
        Image(
            modifier =
                Modifier
                    .size(11.dp, 24.dp)
                    .rotate(180f),
            painter = painterResource(id = R.drawable.ic_arrow_back),
            contentDescription = "타임캡슐 보관하기",
        )
    }
}
