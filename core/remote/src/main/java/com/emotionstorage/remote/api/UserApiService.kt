package com.emotionstorage.remote.api

import com.emotionstorage.remote.response.ResponseDto
import com.emotionstorage.remote.response.myPage.AccountInfoResponse
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH

interface UserApiService {
    @PATCH("/api/v1/mypage/nickname")
    suspend fun updateNickName(
        @Body request: RequestBody,
    ): ResponseDto<Unit>

    @GET("/api/v1/mypage/profile")
    suspend fun getAccountInfo(): ResponseDto<AccountInfoResponse>
}
