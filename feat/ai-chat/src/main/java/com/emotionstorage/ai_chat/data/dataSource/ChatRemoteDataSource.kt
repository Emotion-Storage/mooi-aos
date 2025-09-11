package com.emotionstorage.ai_chat.data.dataSource

interface ChatRemoteDataSource {
    suspend fun getChatRoomId(): String
}