package com.emotionstorage.ai_chat.remote.dataSource

import com.emotionstorage.ai_chat.data.dataSource.remote.ChatRemoteDataSource
import com.emotionstorage.ai_chat.remote.api.ChatApiService
import javax.inject.Inject

class ChatRemoteDataSourceImpl
    @Inject
    constructor(
        private val chatApiService: ChatApiService,
    ) : ChatRemoteDataSource {
        override suspend fun getChatRoomId(): Long {
            val response = chatApiService.postEmotionConversationStart()
            response.data?.roomId?.run {
                return this
            } ?: throw Throwable("getChatRoomId() failed, no room id received!")
        }

        override suspend fun exitChatRoom(roomId: Long): Boolean {
            val response = chatApiService.exitEmotionConversation(roomId)
            response.data?.finished?.run {
                return this
            } ?: throw Throwable("exitChatRoom() failed, no finished received!")
        }
    }
