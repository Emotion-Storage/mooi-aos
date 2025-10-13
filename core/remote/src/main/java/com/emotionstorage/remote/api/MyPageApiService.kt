package com.emotionstorage.remote.api

import com.emotionstorage.remote.request.myPage.NotificationSettingsParam
import com.emotionstorage.remote.response.ResponseDto
import com.emotionstorage.remote.response.myPage.MyPageOverViewResponse
import com.emotionstorage.remote.response.myPage.NotificationSettingsResponse
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH

interface MyPageApiService {
    @GET("api/v1/mypage")
    suspend fun getMyPageOverview(): ResponseDto<MyPageOverViewResponse>

    @GET("api/v1/mypage/notification-settings")
    suspend fun getNotificationSettings(): ResponseDto<NotificationSettingsResponse>

    @PATCH("api/v1/mypage/notification-settings")
    suspend fun updateNotificationSettings(@Body body: RequestBody): ResponseDto<Unit>
}
