package com.emotionstorage.home.ui.model

import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class DayPalette(
    val bg: Brush,
    val border: BorderStroke?,
    val titleColor: Color,
    val contentColor: Color,
    val glowColor: Color? = null,
    val glowElevationDp: Dp = 0.dp,
)
