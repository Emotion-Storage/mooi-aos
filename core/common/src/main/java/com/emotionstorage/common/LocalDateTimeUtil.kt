package com.emotionstorage.common

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun LocalDateTime.formatToKorDateTime(): String =
    this.format(DateTimeFormatter.ofPattern("yyyy.MM.dd")) + " " + this.formatToKorTime()

fun LocalDateTime.formatToKorTime(): String =
    (if (this.hour >= 12) "오후" else "오전") + " " + DateTimeFormatter.ofPattern("hh:mm").format(this)

fun LocalDateTime.toEpochMillis(zoneId: String = "Asia/Seoul"): Long =
    this.atZone(ZoneId.of(zoneId)).toInstant().toEpochMilli()
