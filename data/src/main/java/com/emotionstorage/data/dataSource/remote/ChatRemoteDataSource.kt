package com.emotionstorage.data.dataSource.remote

import com.emotionstorage.data.model.StartEmotionConversationEntity
import com.emotionstorage.domain.common.DataState

interface ChatRemoteDataSource {
    suspend fun startEmotionConversation(): DataState<StartEmotionConversationEntity>

    suspend fun exitChatRoom(roomId: Long): DataState<Boolean>

    suspend fun tempSaveChatRoom(roomId: Long): DataState<Long>
}
