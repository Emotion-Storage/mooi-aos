package com.emotionstorage.ai_chat.remote.api

import com.emotionstorage.ai_chat.remote.response.StartEmotionConversationResponse
import com.emotionstorage.remote.interceptor.AuthRequest
import com.emotionstorage.remote.response.ResponseDto
import retrofit2.http.POST

interface ChatApiService {

    @AuthRequest
    @POST("/api/v1/home/emotion-conversation/test")
    suspend fun postEmotionConversationStart(
    ): ResponseDto<StartEmotionConversationResponse>

}