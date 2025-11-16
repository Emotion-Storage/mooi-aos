package com.emotionstorage.ai_chat.data.repoImpl

import com.emotionstorage.ai_chat.data.dataSource.remote.ChatRemoteDataSource
import com.emotionstorage.ai_chat.data.dataSource.remote.ChatWSDataSource
import com.emotionstorage.domain.model.ChatMessage
import com.emotionstorage.domain.repo.ChatRepository
import com.emotionstorage.domain.common.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ChatRepositoryImpl
    @Inject
    constructor(
        private val chatRemoteDataSource: ChatRemoteDataSource,
        private val chatWSDataSource: ChatWSDataSource,
    ) : ChatRepository {
        override suspend fun getChatRoomId(): Flow<DataState<Long>> =
            flow {
                emit(DataState.Loading(isLoading = true))
                try {
                    val roomId = chatRemoteDataSource.getChatRoomId()
                    emit(DataState.Success(roomId))
                } catch (e: Exception) {
                    emit(DataState.Error(e))
                } finally {
                    emit(DataState.Loading(isLoading = false))
                }
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
    }
