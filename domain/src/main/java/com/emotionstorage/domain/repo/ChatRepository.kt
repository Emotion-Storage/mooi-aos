package com.emotionstorage.domain.repo

import com.emotionstorage.domain.model.ChatMessage
import com.emotionstorage.domain.common.DataState
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun getChatRoomId(): Flow<DataState<String>>

    suspend fun connectChatRoom(roomId: String): Flow<DataState<Boolean>>

    suspend fun disconnectChatRoom(roomId: String): Flow<DataState<Boolean>>

    suspend fun observeChatMessages(roomId: String): Flow<ChatMessage>

    suspend fun sendChatMessage(
        roomId: String,
        chatMessage: ChatMessage,
    ): Flow<DataState<Boolean>>
}
