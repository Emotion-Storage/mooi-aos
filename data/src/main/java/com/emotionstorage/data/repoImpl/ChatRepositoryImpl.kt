package com.emotionstorage.data.repoImpl

import com.emotionstorage.data.dataSource.remote.ChatRemoteDataSource
import com.emotionstorage.data.dataSource.remote.ChatWebSocketDataSource
import com.emotionstorage.data.modelMapper.ChatMessageMapper
import com.emotionstorage.data.modelMapper.StartEmotionConversationMapper
import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.model.ChatMessage
import com.emotionstorage.domain.model.EmotionChatSession
import com.emotionstorage.domain.repo.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChatRepositoryImpl
    @Inject
    constructor(
        private val chatRemote: ChatRemoteDataSource,
        private val chatWebSocket: ChatWebSocketDataSource,
    ) : ChatRepository {
        override suspend fun startEmotionChat(): DataState<EmotionChatSession> =
            try {
                when (val result = chatRemote.startEmotionChat()) {
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

        override suspend fun connectChatRoom(roomId: Long): DataState<Boolean> =
            try {
                val isConnected = chatWebSocket.connectChatRoom()
                DataState.Success(isConnected)
            } catch (e: Exception) {
                DataState.Error(e)
            }

        override suspend fun disconnectChatRoom(roomId: Long): DataState<Boolean> =
            try {
                chatRemote.deleteChatRoom(roomId)
                val isDisconnected = chatWebSocket.disconnectChatRoom()
                DataState.Success(isDisconnected)
            } catch (e: Exception) {
                DataState.Error(e)
            }

        override suspend fun observeChatMessages(roomId: Long): Flow<ChatMessage> =
            chatWebSocket.observeChatMessages(roomId)

        override suspend fun sendChatMessage(
            roomId: Long,
            chatMessage: ChatMessage,
        ): DataState<Boolean> =
            try {
                val isSent = chatWebSocket.sendChatMessage(chatMessage)
                DataState.Success(isSent)
            } catch (e: Exception) {
                DataState.Error(e)
            }

        override suspend fun tempSaveChatRoom(roomId: Long): DataState<Long> =
            try {
                chatRemote.tempSaveChatRoom(roomId)
            } catch (e: Exception) {
                DataState.Error(e)
            }

        override suspend fun deleteChatRoom(roomId: Long): DataState<Boolean> =
            try {
                chatRemote.deleteChatRoom(roomId)
            } catch (e: Exception) {
                DataState.Error(e)
            }

        override suspend fun getChatRoomMessages(cursor: Long?): DataState<List<ChatMessage>> =
            try {
                when (val result = chatRemote.getChatRoomMessages(cursor)) {
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
