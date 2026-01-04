package com.emotionstorage.data.repoImpl

import com.emotionstorage.data.dataSource.remote.ChatRemoteDataSource
import com.emotionstorage.data.dataSource.remote.ChatWSDataSource
import com.emotionstorage.data.modelMapper.ChatMessageMapper
import com.emotionstorage.data.modelMapper.StartEmotionConversationMapper
import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.model.ChatMessage
import com.emotionstorage.domain.model.EmotionChatSession
import com.emotionstorage.domain.repo.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ChatRepositoryImpl
    @Inject
    constructor(
        private val chatRemoteDataSource: ChatRemoteDataSource,
        private val chatWSDataSource: ChatWSDataSource,
    ) : ChatRepository {
        override suspend fun startEmotionChat(): DataState<EmotionChatSession> =
            try {
                when (val result = chatRemoteDataSource.startEmotionChat()) {
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
                    emit(DataState.Loading(isLoading = false))
                } catch (e: Exception) {
                    emit(DataState.Error(e))
                }
            }

        override suspend fun disconnectChatRoom(roomId: Long): Flow<DataState<Boolean>> =
            flow {
                emit(DataState.Loading(isLoading = true))
                try {
                    chatRemoteDataSource.deleteChatRoom(roomId)
                    val isDisconnected = chatWSDataSource.disconnectChatRoom()
                    emit(DataState.Success(isDisconnected))
                    emit(DataState.Loading(isLoading = false))
                } catch (e: Exception) {
                    emit(DataState.Error(e))
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
                    emit(DataState.Loading(isLoading = false))
                } catch (e: Exception) {
                    emit(DataState.Error(e))
                }
            }

        override suspend fun tempSaveChatRoom(roomId: Long): DataState<Long> =
            try {
                chatRemoteDataSource.tempSaveChatRoom(roomId)
            } catch (e: Exception) {
                DataState.Error(e)
            }

        override suspend fun deleteChatRoom(roomId: Long): DataState<Boolean> =
            try {
                chatRemoteDataSource.deleteChatRoom(roomId)
            } catch (e: Exception) {
                DataState.Error(e)
            }

        override suspend fun getChatRoomMessages(cursor: Long?): DataState<List<ChatMessage>> =
            try {
                when (val result = chatRemoteDataSource.getChatRoomMessages(cursor)) {
                    is DataState.Success -> {
                        val messages = ChatMessageMapper.toDomainMessages(result.data)
                        DataState.Success(messages)
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
    }
