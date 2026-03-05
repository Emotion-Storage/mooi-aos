package com.emotionstorage.remote.response.calendar

import kotlinx.serialization.Serializable

@Serializable
data class CalendarDatesResponse(
    val totalDates: Int,
    val dates: List<String>,
)
