package com.emotionstorage.remote.api

import com.emotionstorage.remote.request.fcm.PostFcmTokenRequest
import com.emotionstorage.remote.response.ResponseDto
import retrofit2.http.POST

interface FcmApiService {
    @POST("api/v1/users/fcm-token")
    suspend fun postFcmToken(
        body: PostFcmTokenRequest,
    ): ResponseDto<Unit>
}
