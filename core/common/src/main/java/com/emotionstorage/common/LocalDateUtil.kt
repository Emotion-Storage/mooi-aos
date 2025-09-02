package com.emotionstorage.common

import java.time.DayOfWeek
import java.time.LocalDate

fun LocalDate.getKorDayOfWeek(): String {
    return when (this.getDayOfWeek()) {
        DayOfWeek.MONDAY -> "월요일"
        DayOfWeek.TUESDAY -> "화요일"
        DayOfWeek.WEDNESDAY -> "수요일"
        DayOfWeek.THURSDAY -> "목요일"
        DayOfWeek.FRIDAY -> "금요일"
        DayOfWeek.SATURDAY -> "토요일"
        DayOfWeek.SUNDAY -> "일요일"
    }
}