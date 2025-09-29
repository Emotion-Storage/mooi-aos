package com.emotionstorage.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.ZoneId

@Composable
fun CountDownTimer(
    deadline: LocalDateTime,
    content: @Composable (hours: Long, minutes: Long, seconds: Long) -> Unit,
) {
    val deadlineMillis = deadline.atZone(ZoneId.of("JST")).toInstant().toEpochMilli()
    var remainingTime by remember {
        mutableStateOf(deadlineMillis - System.currentTimeMillis())
    }

    LaunchedEffect(key1 = deadlineMillis) {
        while (remainingTime > 0) {
            delay(1000L)
            remainingTime = deadlineMillis - System.currentTimeMillis()
        }
    }

    val seconds = (remainingTime / 1000) % 60
    val minutes = (remainingTime / (1000 * 60)) % 60
    val hours = (remainingTime / (1000 * 60 * 60))

    content(hours, minutes, seconds)
}
