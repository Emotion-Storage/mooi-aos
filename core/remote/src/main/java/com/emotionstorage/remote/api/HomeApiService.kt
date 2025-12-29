package com.emotionstorage.remote.api

import com.emotionstorage.remote.response.home.HomeResponse
import com.emotionstorage.remote.response.ResponseDto
import retrofit2.http.GET

interface HomeApiService {
    @GET("/api/v1/home")
    suspend fun getHome(): ResponseDto<HomeResponse>
}
