package com.emotionstorage.home.remote.api

import com.emotionstorage.remote.interceptor.AuthRequest
import com.emotionstorage.remote.response.ResponseDto
import retrofit2.http.GET

interface HomeApiService {
    @AuthRequest
    @GET("/api/v1/home")
    suspend fun getHome(): ResponseDto<HomeApiService>
}