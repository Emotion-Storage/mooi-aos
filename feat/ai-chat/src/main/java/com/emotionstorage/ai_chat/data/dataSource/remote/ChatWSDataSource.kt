package com.emotionstorage.ai_chat.data.dataSource.remote

import com.emotionstorage.domain.model.ChatMessage
import kotlinx.coroutines.flow.Flow

interface ChatWSDataSource {
    suspend fun connectChatRoom(): Boolean

    suspend fun disconnectChatRoom(): Boolean

    suspend fun observeChatMessages(roomId: Long): Flow<String>

    suspend fun sendChatMessage(chatMessage: ChatMessage): Boolean
}
