package com.emotionstorage.home.ui.component

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emotionstorage.home.ui.model.Attendance
import com.emotionstorage.home.ui.model.AttendanceStatus
import com.emotionstorage.home.ui.model.DayPalette
import com.emotionstorage.ui.R
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun DayCircle(day: Attendance) {
    val palette = palette(day)

    Box(
        modifier = Modifier
            .size(86.dp)
            .clip(CircleShape)
            .then(if (palette.border != null) Modifier.border(palette.border, CircleShape) else Modifier)
            .background(palette.bg),
        contentAlignment = Alignment.Center
    ) {
        Column(
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

@Composable
private fun palette(day: Attendance): DayPalette = when (day.status) {
    AttendanceStatus.ATTENDED -> DayPalette(
        bg = SolidColor(Color(MooiTheme.colorScheme.gray700.copy(alpha = 0.2f).toArgb())),
        border = BorderStroke(1.dp, MooiTheme.colorScheme.gray800),
        titleColor = MooiTheme.colorScheme.gray700,
        contentColor = MooiTheme.colorScheme.gray700,
        glowColor = null,
    )

    AttendanceStatus.TODAY -> DayPalette(
        bg = SolidColor(Color(MooiTheme.colorScheme.secondary.copy(alpha = 0.15f).toArgb())),
        border = BorderStroke(1.dp, MooiTheme.colorScheme.secondary),
        titleColor = MooiTheme.colorScheme.secondary,
        contentColor = MooiTheme.colorScheme.secondary,
        glowColor = MooiTheme.colorScheme.secondary,
        glowElevationDp = 10.dp,
    )

    AttendanceStatus.UPCOMING -> DayPalette(
        bg = MooiTheme.brushScheme.subButtonBackground,
        border = null,
        titleColor = MooiTheme.colorScheme.gray500,
        contentColor = MooiTheme.colorScheme.gray300,
        glowColor = null,
    )
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
