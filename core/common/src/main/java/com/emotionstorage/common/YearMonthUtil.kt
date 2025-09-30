package com.emotionstorage.common

import java.time.LocalDate
import java.time.YearMonth

fun YearMonth.getWeekDatesOfTargetMonth(): List<LocalDate> {
    val datesOfCalendar = mutableListOf<LocalDate>()

    var targetDate = LocalDate.of(this.year, this.month, 1)
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
