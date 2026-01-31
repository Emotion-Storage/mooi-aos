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
    referenceHeightDp: Int = 800,
): Dp {
    val config = LocalConfiguration.current
    val minDp = baseDp * 0.9f
    val maxDp = baseDp * 1.1f

    return remember(config.screenHeightDp, baseDp, referenceHeightDp, minDp, maxDp) {
        val ratio = baseDp.value / referenceHeightDp.toFloat()
        val scaled = (config.screenHeightDp * ratio).dp
        scaled.coerceIn(minDp, maxDp)
    }
}
