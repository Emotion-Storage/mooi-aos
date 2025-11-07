package com.emotionstorage.remote.api

import com.emotionstorage.remote.request.fcm.PostFcmTokenRequest
import com.emotionstorage.remote.response.ResponseDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.POST

interface FcmApiService {
    @POST("api/v1/users/fcm-token")
    suspend fun postFcmToken(
        @Body request: PostFcmTokenRequest,
    ): ResponseDto<Unit>

    @DELETE("api/v1/users/fcm-token")
    suspend fun deleteFcmToken(
        @Header("X-FCM-Token") token: String,
    ): ResponseDto<Unit>
}
