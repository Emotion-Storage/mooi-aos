package com.emotionstorage.ai_chat.remote.dataSource

import com.emotionstorage.ai_chat.data.dataSource.remote.ChatRemoteDataSource
import com.emotionstorage.ai_chat.remote.api.ChatApiService
import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.common.ErrorCode
import com.emotionstorage.remote.response.CustomHttpException
import java.io.IOException
import javax.inject.Inject

class ChatRemoteDataSourceImpl
    @Inject
    constructor(
        private val chatApiService: ChatApiService,
    ) : ChatRemoteDataSource {
        override suspend fun getChatRoomId(): DataState<Long> =
            try {
                val response = chatApiService.postEmotionConversationStart()
                response.data?.roomId?.run {
                    DataState.Success(this)
                } ?: DataState.Error(Throwable("getChatRoomId() failed, no room id received!"))
            } catch (e: IOException) {
                if (e !is CustomHttpException) {
                    DataState.Error(e, code = ErrorCode.NETWORK_ERROR)
                } else {
                    DataState.Error(e, code = ErrorCode.toErrorCode(e.code ?: ""), data = e.data)
                }
            } catch (e: Exception) {
                DataState.Error(e)
            }

        override suspend fun exitChatRoom(roomId: Long): Boolean {
            val response = chatApiService.exitEmotionConversation(roomId)
            response.data?.finished?.run {
                return this
            } ?: throw Throwable("exitChatRoom() failed, no finished received!")
        }
    }
