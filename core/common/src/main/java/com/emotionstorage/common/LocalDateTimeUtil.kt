package com.emotionstorage.common

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun LocalDateTime.formatToKorDateTime(): String {
    return this.format(DateTimeFormatter.ofPattern("yyyy.MM.dd")) + " " + this.formatToKorTime()
}

fun LocalDateTime.formatToKorTime(): String {
    return (if (this.hour >= 12) "오후" else "오전") + " " + DateTimeFormatter.ofPattern("hh:mm").format(this)
}
