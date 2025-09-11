package com.emotionstorage.ai_chat.remote.dataSource

import com.emotionstorage.ai_chat.data.dataSource.ChatRemoteDataSource
import com.emotionstorage.ai_chat.remote.api.ChatApiService
import javax.inject.Inject

class ChatRemoteDataSourceImpl @Inject constructor(
    private val chatApiService: ChatApiService
) : ChatRemoteDataSource {
    override suspend fun getChatRoomId(): String {
        val response = chatApiService.postEmotionConversationStart()
        response.data?.roomId?.run {
            return this
        } ?: throw Throwable("getChatRoomId() failed, no room id received!")
    }
}