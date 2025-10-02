package com.emotionstorage.ai_chat.data.repoImpl

import com.emotionstorage.ai_chat.data.dataSource.remote.ChatRemoteDataSource
import com.emotionstorage.ai_chat.data.dataSource.remote.ChatWSDataSource
import com.emotionstorage.ai_chat.domain.model.ChatMessage
import com.emotionstorage.ai_chat.domain.repo.ChatRepository
import com.emotionstorage.domain.common.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ChatRepositoryImpl
    @Inject
    constructor(
        private val chatRemoteDataSource: ChatRemoteDataSource,
        private val chatWSDataSource: ChatWSDataSource,
    ) : ChatRepository {
        override suspend fun getChatRoomId(): Flow<DataState<String>> =
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

        override suspend fun connectChatRoom(roomId: String): Flow<DataState<Boolean>> =
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

        override suspend fun disconnectChatRoom(roomId: String): Flow<DataState<Boolean>> =
            flow {
                emit(DataState.Loading(isLoading = true))
                try {
                    val isDisconnected = chatWSDataSource.disconnectChatRoom()
                    emit(DataState.Success(isDisconnected))
                } catch (e: Exception) {
                    emit(DataState.Error(e))
                } finally {
                    emit(DataState.Loading(isLoading = false))
                }
            }

        override suspend fun observeChatMessages(roomId: String): Flow<ChatMessage> =
            chatWSDataSource.observeChatMessages(roomId).map {
                ChatMessage(
                    roomId = roomId,
                    source = ChatMessage.MessageSource.SERVER,
                    content = it,
                )
            }

        override suspend fun sendChatMessage(
            roomId: String,
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
