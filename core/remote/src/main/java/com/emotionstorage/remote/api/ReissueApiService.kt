package com.emotionstorage.remote.api

import com.emotionstorage.remote.response.ResponseDto
import com.emotionstorage.remote.response.auth.ReissueResponseData
import retrofit2.http.POST

interface ReissueApiService {
    /**
     * send refresh token via HttpOnly Cookie
     */
    @POST("/auth/reissue")
    suspend fun postReissue(
    ): ResponseDto<ReissueResponseData>
}
