package com.emotionstorage.data.dataSource.remote

import com.emotionstorage.data.model.EmotionChatSessionEntity
import com.emotionstorage.domain.common.DataState

interface ChatRemoteDataSource {
    suspend fun startEmotionConversation(): DataState<EmotionChatSessionEntity>

    suspend fun exitChatRoom(roomId: Long): DataState<Boolean>

    suspend fun tempSaveChatRoom(roomId: Long): DataState<Long>
}
