package com.emotionstorage.domain.model

import java.time.LocalDate

data class CalendarDates(
    val year: Int,
    val month: Int,
    val dates: Set<LocalDate>,
)
