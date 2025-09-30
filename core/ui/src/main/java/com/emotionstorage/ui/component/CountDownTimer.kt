package com.emotionstorage.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.emotionstorage.common.toEpochMillis
import kotlinx.coroutines.delay
import java.time.LocalDateTime

@Composable
fun CountDownTimer(
    deadline: LocalDateTime,
    // tick duration set to 1h if optimized & remaining time >= 1h
    optimizeMinuteTick: Boolean = false,
    // tick duration set to 1m if optimized & remaining time >= 1m
    optimizeSecondTick: Boolean = false,
    content: @Composable (hours: Long, minutes: Long, seconds: Long) -> Unit,
) {
    var remainingTime by remember(key1 = deadline) {
        mutableLongStateOf(deadline.toEpochMillis() - LocalDateTime.now().toEpochMillis())
    }

    LaunchedEffect(key1 = deadline) {
        if (remainingTime <= 0) remainingTime = 0

        while (remainingTime > 0) {
            val tick =
                if (optimizeMinuteTick && remainingTime >= 60 * 60 * 1000) {
                    60 * 60 * 1000
                } else if (optimizeSecondTick && remainingTime >= 60 * 1000) {
                    60 * 1000
                } else {
                    1000
                }
            delay(tick.toLong())

            remainingTime = deadline.toEpochMillis() - LocalDateTime.now().toEpochMillis()
        }
    }

    val seconds = (remainingTime / 1000) % 60
    val minutes = (remainingTime / (1000 * 60)) % 60
    val hours = (remainingTime / (1000 * 60 * 60))

    content(hours, minutes, seconds)
}
