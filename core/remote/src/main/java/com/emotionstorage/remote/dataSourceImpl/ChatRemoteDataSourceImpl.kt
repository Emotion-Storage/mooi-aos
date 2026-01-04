package com.emotionstorage.remote.dataSourceImpl

import com.emotionstorage.data.dataSource.remote.ChatRemoteDataSource
import com.emotionstorage.data.model.ChatRoomMessagesEntity
import com.emotionstorage.data.model.EmotionChatSessionEntity
import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.common.ErrorCode
import com.emotionstorage.remote.api.ChatApiService
import com.emotionstorage.remote.modelMapper.ChatRoomMessagesMapper
import com.emotionstorage.remote.modelMapper.EmotionChatSessionMapper
import com.emotionstorage.remote.response.CustomHttpException
import com.orhanobut.logger.Logger
import java.io.IOException
import javax.inject.Inject

class ChatRemoteDataSourceImpl
    @Inject
    constructor(
        private val chatApiService: ChatApiService,
    ) : ChatRemoteDataSource {
        override suspend fun startEmotionChat(): DataState<EmotionChatSessionEntity> =
            try {
                val response = chatApiService.postEmotionConversationStart()
                response.data?.run {
                    DataState.Success(EmotionChatSessionMapper.toData(this))
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

        override suspend fun deleteChatRoom(roomId: Long): DataState<Boolean> =
            try {
                val response = chatApiService.deleteEmotionConversation(roomId)
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

        override suspend fun getChatRoomMessages(cursor: Long?): DataState<ChatRoomMessagesEntity> =
            try {
                val response = chatApiService.getChatRoomMessages(cursor = cursor)
                Logger.d(response.data.toString())
                response.data?.let { DataState.Success(ChatRoomMessagesMapper.toData(it)) }
                    ?: DataState.Error(Throwable("getChatRoomMessages() failed, no data received!"))
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
