package com.emotionstorage.remote.api

import com.emotionstorage.remote.request.fcm.FcmTokenRequest
import com.emotionstorage.remote.response.ResponseDto
import retrofit2.http.DELETE
import retrofit2.http.POST

interface FcmApiService {
    @POST("api/v1/users/fcm-token")
    suspend fun postFcmToken(
        body: FcmTokenRequest,
    ): ResponseDto<Unit>

    @DELETE("api/v1/users/fcm-token")
    suspend fun deleteFcmToken(
        body: FcmTokenRequest,
    ): ResponseDto<Unit>
}
