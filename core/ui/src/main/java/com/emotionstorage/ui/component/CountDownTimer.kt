package com.emotionstorage.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.ZoneId

@Composable
fun CountDownTimer(
    deadline: LocalDateTime,
    // tick duration set to 1h if optimized & remaining time >= 1h
    optimizeMinuteTick: Boolean = false,
    // tick duration set to 1m if optimized & remaining time >= 1m
    optimizeSecondTick: Boolean = false,
    content: @Composable (hours: Long, minutes: Long, seconds: Long) -> Unit,
) {
    val deadlineMillis = deadline.atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli()

    var remainingTime by remember(key1 = deadlineMillis) {
        mutableLongStateOf(deadlineMillis - System.currentTimeMillis())
    }

    LaunchedEffect(key1 = deadlineMillis) {
        if (remainingTime <= 0) remainingTime = 0

        while (remainingTime > 0) {
            val tick =
                if (optimizeMinuteTick && remainingTime >= 60 * 60 * 1000) {
                    60 * 60 * 1000
                } else if (optimizeSecondTick && remainingTime >= 60 * 1000) {
                    60 * 1000
                } else 1000
            delay(tick.toLong())

            remainingTime = deadlineMillis - System.currentTimeMillis()
        }
    }

    val seconds = (remainingTime / 1000) % 60
    val minutes = (remainingTime / (1000 * 60)) % 60
    val hours = (remainingTime / (1000 * 60 * 60))

    content(hours, minutes, seconds)
}
