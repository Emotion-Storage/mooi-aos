package com.emotionstorage.data.repoImpl

import com.emotionstorage.data.dataSource.local.ChatTempSaveLocalDataSource
import com.emotionstorage.data.dataSource.remote.ChatRemoteDataSource
import com.emotionstorage.data.dataSource.remote.ChatWSDataSource
import com.emotionstorage.data.modelMapper.StartEmotionConversationMapper
import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.model.ChatMessage
import com.emotionstorage.domain.model.EmotionConversationStartInfo
import com.emotionstorage.domain.repo.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ChatRepositoryImpl
    @Inject
    constructor(
        private val chatRemoteDataSource: ChatRemoteDataSource,
        private val chatWSDataSource: ChatWSDataSource,
        private val chatTempSaveLocal: ChatTempSaveLocalDataSource,
    ) : ChatRepository {
        override suspend fun startEmotionConversation(): DataState<EmotionConversationStartInfo> =
            try {
                when (val result = chatRemoteDataSource.startEmotionConversation()) {
                    is DataState.Success -> {
                        DataState.Success(StartEmotionConversationMapper.toDomain(result.data))
                    }

                    is DataState.Loading -> {
                        DataState.Loading(result.isLoading)
                    }

                    is DataState.Error -> {
                        DataState.Error(result.throwable, result.code, result.data)
                    }
                }
            } catch (e: Exception) {
                DataState.Error(e)
            }

        override suspend fun connectChatRoom(roomId: Long): Flow<DataState<Boolean>> =
            flow {
                emit(DataState.Loading(isLoading = true))
                try {
                    val isConnected = chatWSDataSource.connectChatRoom()
                    emit(DataState.Success(isConnected))
                } catch (e: Exception) {
                    emit(DataState.Error(e))
                } finally {
                    emit(DataState.Loading(isLoading = false))
                }
            }

        override suspend fun disconnectChatRoom(roomId: Long): Flow<DataState<Boolean>> =
            flow {
                emit(DataState.Loading(isLoading = true))
                try {
                    chatRemoteDataSource.exitChatRoom(roomId)
                    val isDisconnected = chatWSDataSource.disconnectChatRoom()
                    emit(DataState.Success(isDisconnected))
                } catch (e: Exception) {
                    emit(DataState.Error(e))
                } finally {
                    emit(DataState.Loading(isLoading = false))
                }
            }

        override suspend fun observeChatMessages(roomId: Long): Flow<ChatMessage> =
            chatWSDataSource.observeChatMessages(roomId)

        override suspend fun sendChatMessage(
            roomId: Long,
            chatMessage: ChatMessage,
        ): Flow<DataState<Boolean>> =
            flow {
                emit(DataState.Loading(isLoading = true))
                try {
                    val isSent = chatWSDataSource.sendChatMessage(chatMessage)
                    emit(DataState.Success(isSent))
                } catch (e: Exception) {
                    emit(DataState.Error(e))
                } finally {
                    emit(DataState.Loading(isLoading = true))
                }
            }

        override suspend fun tempSaveChatRoom(roomId: Long): DataState<Long> =
            try {
                chatRemoteDataSource.tempSaveChatRoom(roomId)
            } catch (e: Exception) {
                DataState.Error(e)
            }

        override val tempSavedRoomId = chatTempSaveLocal.tempSavedRoomId

        override suspend fun saveTempSavedRoomId(roomId: Long) {
            chatTempSaveLocal.setTempSavedRoomId(roomId)
        }

        override suspend fun clearTempSavedRoomId() {
            chatTempSaveLocal.clearTempSavedRoomId()
        }
    }
