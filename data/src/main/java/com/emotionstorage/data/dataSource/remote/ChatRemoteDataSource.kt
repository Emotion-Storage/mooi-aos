package com.emotionstorage.data.dataSource.remote

import com.emotionstorage.data.model.EmotionChatSessionEntity
import com.emotionstorage.domain.common.DataState

interface ChatRemoteDataSource {
    suspend fun startEmotionChat(): DataState<EmotionChatSessionEntity>

    suspend fun exitChatRoom(roomId: Long): DataState<Boolean>

    suspend fun tempSaveChatRoom(roomId: Long): DataState<Long>
}
