package com.emotionstorage.home.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.emotionstorage.home.ui.model.Attendance
import com.emotionstorage.home.ui.model.AttendanceStatus
import com.emotionstorage.ui.R
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun DayCircle(day: Attendance) {
    val palette = palette(day)
    val maskBase = MooiTheme.colorScheme.background

    // 최 상위에 Clip을 줄 시 색상이 겹쳐보이게 됨
    Box(
        modifier = Modifier
            .size(86.dp)
            .then(
                if (palette.glowColor != null)
                    Modifier.dropShadow(
                        shape = CircleShape,
                        shadow = Shadow(
                            radius = 8.dp,
                            color = palette.glowColor,
                            offset = DpOffset(0.dp, 0.dp),
                            spread = 0.dp,
                            alpha = 1f
                        )
                    )
                else Modifier
            )
    ) {
        Box(
            Modifier
                .matchParentSize()
                .clip(CircleShape)
                .background(maskBase)
        )

        Box(
            Modifier
                .matchParentSize()
                .clip(CircleShape)
                .background(palette.bg)
                .then(if (palette.border != null) Modifier.border(palette.border, CircleShape) else Modifier)
        )

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${day.day}일차",
                style = MooiTheme.typography.caption3,
                color = palette.titleColor,
            )
            Spacer(modifier = Modifier.size(6.dp))
            Row(
                modifier = Modifier.height(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.key),
                    contentDescription = "열쇠",
                    modifier = Modifier.size(20.dp),
                    tint = palette.contentColor,
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = "X${day.rewardKeys}",
                    style = MooiTheme.typography.body5,
                    color = palette.contentColor,
                )
            }
        }
    }
}

@Preview
@Composable
private fun DayCirclePreview() {
    MooiTheme {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            DayCircle(
                Attendance(
                    day = 1,
                    rewardKeys = 1,
                    status = AttendanceStatus.ATTENDED
                )
            )

            Spacer(modifier = Modifier.size(24.dp))

            DayCircle(
                Attendance(
                    day = 2,
                    rewardKeys = 1,
                    status = AttendanceStatus.TODAY
                )
            )

            Spacer(modifier = Modifier.size(24.dp))

            DayCircle(
                Attendance(
                    day = 1,
                    rewardKeys = 1,
                    status = AttendanceStatus.UPCOMING
                )
            )
        }
    }
}
