package com.emotionstorage.remote.response.calendar

data class CalendarDatesResponse(
    val totalDates: Int,
    val dates: List<String>,
)
