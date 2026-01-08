package com.emotionstorage.data.dataSource.remote

import com.emotionstorage.data.model.ChatRoomMessagesEntity
import com.emotionstorage.data.model.EmotionChatSessionEntity
import com.emotionstorage.domain.common.DataState

interface ChatRemoteDataSource {
    suspend fun startEmotionChat(): DataState<EmotionChatSessionEntity>

    suspend fun deleteChatRoom(roomId: Long): DataState<Boolean>

    suspend fun tempSaveChatRoom(roomId: Long): DataState<Long>

    suspend fun getChatRoomMessages(cursor: Long? = null): DataState<ChatRoomMessagesEntity>
}
