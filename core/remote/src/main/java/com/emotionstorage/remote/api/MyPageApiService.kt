package com.emotionstorage.remote.api

import com.emotionstorage.remote.response.ResponseDto
import com.emotionstorage.remote.response.myPage.MyPageOverViewResponse
import retrofit2.http.GET

interface MyPageApiService {
    @GET("api/v1/mypage")
    suspend fun getMyPageOverview(): ResponseDto<MyPageOverViewResponse>
}
