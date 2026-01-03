package com.emotionstorage.domain.repo

import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.model.ChatMessage
import com.emotionstorage.domain.model.EmotionConversationStartInfo
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun startEmotionConversation(): DataState<EmotionConversationStartInfo>

    suspend fun connectChatRoom(roomId: Long): Flow<DataState<Boolean>>

    suspend fun disconnectChatRoom(roomId: Long): Flow<DataState<Boolean>>

    suspend fun observeChatMessages(roomId: Long): Flow<ChatMessage>

    suspend fun sendChatMessage(
        roomId: Long,
        chatMessage: ChatMessage,
    ): Flow<DataState<Boolean>>

    suspend fun tempSaveChatRoom(roomId: Long): DataState<Long>

    val tempSavedRoomId: Flow<Long?>

    suspend fun saveTempSavedRoomId(roomId: Long)

    suspend fun clearTempSavedRoomId()
}
