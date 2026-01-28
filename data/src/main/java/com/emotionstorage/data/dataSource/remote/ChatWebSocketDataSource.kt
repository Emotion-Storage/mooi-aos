package com.emotionstorage.data.dataSource.remote

import com.emotionstorage.domain.model.ChatMessage
import kotlinx.coroutines.flow.Flow

interface ChatWebSocketDataSource {
    suspend fun connectChatRoom(): Boolean

    suspend fun disconnectChatRoom(): Boolean

    suspend fun observeChatMessages(roomId: Long): Flow<ChatMessage>

    suspend fun sendChatMessage(chatMessage: ChatMessage): Boolean
}
