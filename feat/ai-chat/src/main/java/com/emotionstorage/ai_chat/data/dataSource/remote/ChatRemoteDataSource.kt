package com.emotionstorage.ai_chat.data.dataSource.remote

interface ChatRemoteDataSource {
    suspend fun getChatRoomId(): String
}
