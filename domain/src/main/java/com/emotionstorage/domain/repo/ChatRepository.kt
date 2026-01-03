package com.emotionstorage.domain.repo

import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.model.ChatMessage
import com.emotionstorage.domain.model.EmotionChatSession
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun startEmotionChat(): DataState<EmotionChatSession>

    // Chat websocket 연결 용
    suspend fun connectChatRoom(roomId: Long): Flow<DataState<Boolean>>

    // Chat websocket 해제 용
    suspend fun disconnectChatRoom(roomId: Long): Flow<DataState<Boolean>>

    suspend fun observeChatMessages(roomId: Long): Flow<ChatMessage>

    suspend fun sendChatMessage(
        roomId: Long,
        chatMessage: ChatMessage,
    ): Flow<DataState<Boolean>>

    suspend fun tempSaveChatRoom(roomId: Long): DataState<Long>

//    suspend fun getChatRoomMessages(cursor: Long? = null): DataState<List<ChatMessage>>
}
