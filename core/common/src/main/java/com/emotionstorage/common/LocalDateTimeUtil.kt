package com.emotionstorage.common

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

fun LocalDateTime.formatToKorDateTime(
    datePattern: String = "yyyy.MM.dd",
    addDoubleSpacing: Boolean = false,
): String = this.format(DateTimeFormatter.ofPattern(datePattern)) + " " + this.formatToKorTime(addDoubleSpacing)

fun LocalDateTime.formatToKorTime(addDoubleSpacing: Boolean = false): String =
    (if (this.hour >= 12) "오후" else "오전") + (if (addDoubleSpacing) "  " else " ") +
        DateTimeFormatter
            .ofPattern("hh:mm")
            .format(this)

fun LocalDateTime.toEpochMillis(zoneId: String = "Asia/Seoul"): Long =
    this.atZone(ZoneId.of(zoneId)).toInstant().toEpochMilli()

fun LocalDateTime.timeAgo(): String {
    val now = LocalDateTime.now()

    val minutes = ChronoUnit.MINUTES.between(this, now)
    val hours = ChronoUnit.HOURS.between(this, now)
    val days = ChronoUnit.DAYS.between(this, now)

    return when {
        minutes < 60 -> "${minutes}분 전"
        hours < 24 -> "${hours}시간 전"
        else -> "${days}일 전"
    }
}
