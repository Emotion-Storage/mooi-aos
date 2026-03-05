package com.emotionstorage.remote.api

import com.emotionstorage.remote.response.ResponseDto
import com.emotionstorage.remote.response.calendar.CalendarDatesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CalendarApiService {
    @GET("api/v1/calendar/date")
    suspend fun getTimeCapsuleOrDailyReportExistDate(
        @Query("year") year: Int,
        @Query("month") month: Int,
    ): ResponseDto<CalendarDatesResponse>
}
