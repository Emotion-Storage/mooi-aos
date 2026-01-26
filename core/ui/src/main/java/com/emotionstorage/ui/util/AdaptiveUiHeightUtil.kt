package com.emotionstorage.ui.util

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun rememberAdaptiveHeightDp(
    baseDp: Dp,
    referenceHeightDp: Int = 752,
    minDp: Dp = 0.dp,
    maxDp: Dp = 1000.dp,
): Dp {
    val config = LocalConfiguration.current

    return remember(config.screenHeightDp, baseDp, referenceHeightDp, minDp, maxDp) {
        val ratio = baseDp.value / referenceHeightDp.toFloat()
        val scaled = (config.screenHeightDp * ratio).dp
        scaled.coerceIn(minDp, maxDp)
    }
}
