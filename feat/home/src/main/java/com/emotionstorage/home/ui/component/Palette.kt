package com.emotionstorage.home.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.emotionstorage.domain.model.AttendanceSummary
import com.emotionstorage.home.ui.model.DayPalette
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun palette(day: AttendanceSummary.Attendance): DayPalette =
    when (day.status) {
        AttendanceSummary.AttendanceStatus.ATTENDED ->
            DayPalette(
                bg =
                    SolidColor(
                        Color(
                            MooiTheme
                                .colorScheme
                                .gray700
                                .copy(alpha = 0.2f)
                                .toArgb(),
                        ),
                    ),
                border = BorderStroke(1.dp, MooiTheme.colorScheme.gray800),
                titleColor = MooiTheme.colorScheme.gray700,
                contentColor = MooiTheme.colorScheme.gray700,
                glowColor = null,
            )

        AttendanceSummary.AttendanceStatus.TODAY ->
            DayPalette(
                bg =
                    SolidColor(
                        Color(
                            MooiTheme
                                .colorScheme
                                .secondary
                                .copy(alpha = 0.15f)
                                .toArgb(),
                        ),
                    ),
                border = BorderStroke(1.dp, MooiTheme.colorScheme.secondary),
                titleColor = MooiTheme.colorScheme.secondary,
                contentColor = MooiTheme.colorScheme.secondary,
                glowColor = MooiTheme.colorScheme.secondary,
                glowElevationDp = 10.dp,
            )

        AttendanceSummary.AttendanceStatus.UPCOMING ->
            DayPalette(
                bg = MooiTheme.brushScheme.subButtonBackground,
                border = null,
                titleColor = MooiTheme.colorScheme.gray500,
                contentColor = MooiTheme.colorScheme.gray300,
                glowColor = null,
            )
    }
