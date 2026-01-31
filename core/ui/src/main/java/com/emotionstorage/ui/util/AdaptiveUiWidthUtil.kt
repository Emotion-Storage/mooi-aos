package com.emotionstorage.ui.util

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun rememberAdaptiveWidthDp(
    baseDp: Dp,
    referenceWidthDp: Int = 360,
): Dp {
    val config = LocalConfiguration.current
    val minDp = baseDp * 0.9f
    val maxDp = baseDp * 1.1f

    return remember(config.screenWidthDp, baseDp, referenceWidthDp, minDp, maxDp) {
        val ratio = baseDp.value / referenceWidthDp.toFloat()
        val scaled = (config.screenWidthDp * ratio).dp
        scaled.coerceIn(minDp, maxDp)
    }
}
