package com.emotionstorage.data.dataSource.remote

import com.emotionstorage.domain.common.DataState

interface ChatRemoteDataSource {
    suspend fun getChatRoomId(): DataState<Long>

    suspend fun exitChatRoom(roomId: Long): DataState<Boolean>

    suspend fun tempSaveChatRoom(roomId: Long): DataState<Long>
}
