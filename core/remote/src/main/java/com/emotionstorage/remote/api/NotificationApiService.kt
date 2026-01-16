package com.emotionstorage.remote.api

import com.emotionstorage.remote.response.ResponseDto
import com.emotionstorage.remote.response.notification.GetNotificationsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NotificationApiService {
    @GET("api/v1/notifications")
    suspend fun getNotifications(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
    ): ResponseDto<GetNotificationsResponse>
}
