package com.emotionstorage.common

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun LocalDateTime.formatToKorDateTime(
    datePattern: String = "yyyy.MM.dd",
    addDoubleSpacing: Boolean = false,
): String =
    this.format(DateTimeFormatter.ofPattern(datePattern)) + " " + this.formatToKorTime(addDoubleSpacing)

fun LocalDateTime.formatToKorTime(
    addDoubleSpacing: Boolean = false,
): String =
    (if (this.hour >= 12) "오후" else "오전") + (if (addDoubleSpacing) "  " else " ") + DateTimeFormatter.ofPattern("hh:mm")
        .format(this)

fun LocalDateTime.toEpochMillis(zoneId: String = "Asia/Seoul"): Long =
    this.atZone(ZoneId.of(zoneId)).toInstant().toEpochMilli()
