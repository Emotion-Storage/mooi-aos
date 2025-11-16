package com.emotionstorage.ai_chat.remote.api

import com.emotionstorage.ai_chat.remote.response.ExitChatRoomResponse
import com.emotionstorage.ai_chat.remote.response.StartEmotionConversationResponse
import com.emotionstorage.remote.interceptor.AuthRequest
import com.emotionstorage.remote.response.ResponseDto
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path

interface ChatApiService {
    @AuthRequest
    @POST("/api/v1/home/emotion-conversation")
    suspend fun postEmotionConversationStart(): ResponseDto<StartEmotionConversationResponse>

    @AuthRequest
    @DELETE("/api/v1/home/emotion-conversation/{roomId}")
    suspend fun exitEmotionConversation(
        @Path("roomId") roomId: Long,
    ): ResponseDto<ExitChatRoomResponse>
}
