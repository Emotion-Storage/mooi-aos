package com.emotionstorage.common

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.ChronoUnit

fun LocalDate.getDaysBetween(other: LocalDate): Int = ChronoUnit.DAYS.between(this, other).toInt()

fun LocalDate.toKorDate(): String = "${this.year}년 ${this.monthValue}월 ${this.dayOfMonth}일 ${this.getKorDayOfWeek()}"

fun LocalDate.getKorDayOfWeek(): String =
    when (this.getDayOfWeek()) {
        DayOfWeek.MONDAY -> "월요일"
        DayOfWeek.TUESDAY -> "화요일"
        DayOfWeek.WEDNESDAY -> "수요일"
        DayOfWeek.THURSDAY -> "목요일"
        DayOfWeek.FRIDAY -> "금요일"
        DayOfWeek.SATURDAY -> "토요일"
        DayOfWeek.SUNDAY -> "일요일"
    }

fun LocalDate.getWeekDatesOfTargetMonth(): List<LocalDate> {
    val datesOfCalendar = mutableListOf<LocalDate>()

    var targetDate = this.withDayOfMonth(1)
    for (limit in 0..6) { // limit to prevent infinite loop
        val weekDates = targetDate.getWeekDatesOfTargetDate()
        if (weekDates.all { it.month != this.month }) {
            return datesOfCalendar
        } else {
            datesOfCalendar.addAll(weekDates)
        }
        targetDate = targetDate.plusDays(7)
    }
    return datesOfCalendar
}

fun LocalDate.getWeekDatesOfTargetDate(): List<LocalDate> {
    // get week of target date
    val dayOfWeek = this.dayOfWeek
    val sunday = this.minusDays(dayOfWeek.value % 7L)

    // return list of dates of week, from sunday to saturday
    return (0L..6L).map { sunday.plusDays(it) }
}
