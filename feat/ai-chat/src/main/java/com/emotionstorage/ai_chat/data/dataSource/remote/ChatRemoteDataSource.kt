package com.emotionstorage.ai_chat.data.dataSource.remote

import com.emotionstorage.domain.common.DataState

interface ChatRemoteDataSource {
    suspend fun getChatRoomId(): DataState<Long>

    suspend fun exitChatRoom(roomId: Long): DataState<Boolean>

    suspend fun tempSaveChatRoom(roomId: Long) : DataState<Long>
}
