package com.emotionstorage.ai_chat.data.dataSource

import com.emotionstorage.ai_chat.domain.model.ChatMessage
import kotlinx.coroutines.flow.Flow

interface ChatWSDataSource {
    suspend fun connectChatRoom(): Boolean
    suspend fun disconnectChatRoom(): Boolean
    suspend fun observeChatMessages(roomId: String): Flow<String>
    suspend fun sendChatMessage(chatMessage: ChatMessage): Boolean
}
