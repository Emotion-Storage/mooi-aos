package com.emotionstorage.home.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.emotionstorage.domain.model.AttendanceSummary
import com.emotionstorage.ui.R
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun DayCircle(
    day: AttendanceSummary.Attendance,
    modifier: Modifier = Modifier,
) {
    val p = palette(day)
    val isSeventh = day.day == 7

    val shape: Shape = if (isSeventh) RoundedCornerShape(100.dp) else CircleShape
    val base =
        if (isSeventh) {
            modifier
                .fillMaxWidth()
                .height(80.dp)
        } else {
            modifier.size(86.dp)
        }

    DropShadowContainer(
        modifier = base,
        shape = shape,
        glowColor = p.glowColor,
        background = p.bg,
        border = p.border,
        maskBase = MooiTheme.colorScheme.background,
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "${day.day}일차",
                style = MooiTheme.typography.caption3,
                color = p.titleColor,
            )
            Spacer(Modifier.size(6.dp))
            Row(
                modifier = Modifier.height(24.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(R.drawable.key),
                    contentDescription = "열쇠",
                    modifier = Modifier.size(20.dp),
                    tint = p.contentColor,
                )
                Spacer(Modifier.size(4.dp))
                Text(
                    text = "x${day.rewardKeys}",
                    style = MooiTheme.typography.body5,
                    color = p.contentColor,
                )
            }
        }
    }
}

@Composable
private fun DropShadowContainer(
    modifier: Modifier = Modifier,
    shape: Shape,
    glowColor: Color?,
    background: Brush,
    border: BorderStroke?,
    maskBase: Color,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier =
            modifier.then(
                if (glowColor != null) {
                    Modifier.dropShadow(
                        shape = shape,
                        shadow =
                            Shadow(
                                color = glowColor,
                                radius = 8.dp,
                                offset = DpOffset(0.dp, 0.dp),
                                spread = 0.dp,
                                alpha = 1f,
                            ),
                    )
                } else {
                    Modifier
                },
            ),
    ) {
        Box(
            Modifier
                .matchParentSize()
                .clip(shape)
                .background(color = maskBase),
        )

        Box(
            Modifier
                .matchParentSize()
                .clip(shape)
                .background(background)
                .then(if (border != null) Modifier.border(border, shape) else Modifier),
            content = content,
        )
    }
}

@Preview
@Composable
private fun DayCirclePreview() {
    MooiTheme {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                DayCircle(
                    AttendanceSummary.Attendance(
                        day = 1,
                        rewardKeys = 1,
                        status = AttendanceSummary.AttendanceStatus.ATTENDED,
                    ),
                )

                Spacer(modifier = Modifier.size(24.dp))

                DayCircle(
                    AttendanceSummary.Attendance(
                        day = 2,
                        rewardKeys = 1,
                        status = AttendanceSummary.AttendanceStatus.TODAY,
                    ),
                )

                Spacer(modifier = Modifier.size(24.dp))

                DayCircle(
                    AttendanceSummary.Attendance(
                        day = 1,
                        rewardKeys = 1,
                        status = AttendanceSummary.AttendanceStatus.UPCOMING,
                    ),
                )
            }

            Spacer(modifier = Modifier.size(24.dp))

            DayCircle(
                AttendanceSummary.Attendance(
                    day = 7,
                    rewardKeys = 3,
                    status = AttendanceSummary.AttendanceStatus.ATTENDED,
                ),
            )

            Spacer(modifier = Modifier.size(24.dp))

            DayCircle(
                AttendanceSummary.Attendance(
                    day = 7,
                    rewardKeys = 3,
                    status = AttendanceSummary.AttendanceStatus.TODAY,
                ),
            )

            Spacer(modifier = Modifier.size(24.dp))

            DayCircle(
                AttendanceSummary.Attendance(
                    day = 7,
                    rewardKeys = 3,
                    status = AttendanceSummary.AttendanceStatus.UPCOMING,
                ),
            )
        }
    }
}
