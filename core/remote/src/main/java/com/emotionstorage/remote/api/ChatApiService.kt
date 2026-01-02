package com.emotionstorage.remote.api

import com.emotionstorage.remote.response.ResponseDto
import com.emotionstorage.remote.response.chat.ChatMessageTempSaveResponse
import com.emotionstorage.remote.response.chat.ChatRoomMessagesResponse
import com.emotionstorage.remote.response.chat.ExitChatRoomResponse
import com.emotionstorage.remote.response.chat.StartEmotionConversationResponse
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ChatApiService {
    @POST("/api/v1/home/emotion-conversation")
    suspend fun postEmotionConversationStart(): ResponseDto<StartEmotionConversationResponse>

    @DELETE("/api/v1/home/emotion-conversation/{roomId}")
    suspend fun deleteExitEmotionConversation(
        @Path("roomId") roomId: Long,
    ): ResponseDto<ExitChatRoomResponse>

    @PATCH("/api/v1/chat/{roomId}/temp-save")
    suspend fun patchChatRoomTempSave(
        @Path("roomId") roomId: Long,
    ): ResponseDto<ChatMessageTempSaveResponse>

    @GET("/api/v1/chat/rooms")
    suspend fun getChatRoomMessages(
        @Query("cursor") cursor: Long? = null,
    ): ResponseDto<ChatRoomMessagesResponse>
}
