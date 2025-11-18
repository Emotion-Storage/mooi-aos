package com.emotionstorage.remote.api

import com.emotionstorage.remote.response.ResponseDto
import com.emotionstorage.remote.response.dailyReport.GetDailyReportResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface DailyReportApiService {
    @GET("api/v1/daily-report")
    suspend fun getDailyReport(
        // yyyy-MM-dd
        @Query(value = "date") date: String,
    ): ResponseDto<GetDailyReportResponse>

    @GET("api/v1/daily-report/{report-id}")
    suspend fun getDailyReport(
        @Query(value = "report-id") id: Long,
    ): ResponseDto<GetDailyReportResponse>
}
