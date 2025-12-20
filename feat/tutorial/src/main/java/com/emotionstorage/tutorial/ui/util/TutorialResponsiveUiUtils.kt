package com.emotionstorage.tutorial.ui.util

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp

@Immutable
data class TutorialResponsiveTokens(
    val insets: PaddingValues,
    val topPadding: Dp,
    val bottomPadding: Dp,
    val descriptionHeight: Dp,
    val titleHeight: Dp,
)

internal fun Dp.scaledBy(scaleY: Float): Dp = this * scaleY

internal fun Dp.clamp(
    min: Dp,
    max: Dp,
): Dp = this.coerceIn(min, max)
