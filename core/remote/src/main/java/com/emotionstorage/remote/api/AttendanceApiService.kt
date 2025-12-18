package com.emotionstorage.remote.api

import com.emotionstorage.remote.response.ResponseDto
import com.emotionstorage.remote.response.attendance.AttendanceResponse
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AttendanceApiService {
    @POST("/api/v1/attendance/attend/{rewardDate}")
    suspend fun postAttendance(
        @Path(value = "rewardDate") rewardDate: String,
    ): ResponseDto<AttendanceResponse>

    @GET("/api/v1/attendance")
    suspend fun getAttendanceStatus(): ResponseDto<AttendanceResponse>
}
