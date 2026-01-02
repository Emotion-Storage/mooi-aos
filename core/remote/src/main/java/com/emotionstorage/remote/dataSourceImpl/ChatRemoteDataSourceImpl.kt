package com.emotionstorage.remote.dataSourceImpl

import com.emotionstorage.data.dataSource.remote.ChatRemoteDataSource
import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.common.ErrorCode
import com.emotionstorage.remote.api.ChatApiService
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

        override suspend fun exitChatRoom(roomId: Long): DataState<Boolean> =
            try {
                val response = chatApiService.deleteExitEmotionConversation(roomId)
                response.data?.finished?.run {
                    DataState.Success(this)
                } ?: DataState.Error(Throwable("exitChatRoom() failed, no room id received!"))
            } catch (e: IOException) {
                if (e !is CustomHttpException) {
                    DataState.Error(e, code = ErrorCode.NETWORK_ERROR)
                } else {
                    DataState.Error(e, code = ErrorCode.toErrorCode(e.code ?: ""), data = e.data)
                }
            } catch (e: Exception) {
                DataState.Error(e)
            }

        override suspend fun tempSaveChatRoom(roomId: Long): DataState<Long> =
            try {
                val response = chatApiService.patchChatRoomTempSave(roomId)
                response.data?.chatRoomId?.run {
                    DataState.Success(this)
                } ?: DataState.Error(Throwable("tempSaveChatRoom() failed, no chatRoomId received!"))
            } catch (e: IOException) {
                if (e !is CustomHttpException) {
                    DataState.Error(e, code = ErrorCode.NETWORK_ERROR)
                } else {
                    DataState.Error(e, code = ErrorCode.toErrorCode(e.code ?: ""), data = e.data)
                }
            } catch (e: Exception) {
                DataState.Error(e)
            }
    }
