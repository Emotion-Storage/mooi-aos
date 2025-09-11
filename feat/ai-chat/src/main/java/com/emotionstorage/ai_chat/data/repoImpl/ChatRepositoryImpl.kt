package com.emotionstorage.ai_chat.data.repoImpl

import com.emotionstorage.ai_chat.domain.model.ChatMessage
import com.emotionstorage.ai_chat.domain.repo.ChatRepository
import com.emotionstorage.ai_chat.remote.api.ChatApiService
import com.emotionstorage.domain.common.DataState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

// todo: refactor to use remote data source interface instead of api service
class ChatRepositoryImpl @Inject constructor(
    private val chatApiService: ChatApiService
) : ChatRepository {
    override suspend fun getChatRoomId(): Flow<DataState<String>> = flow {
        emit(DataState.Loading(isLoading = true))
        try {
            val response = chatApiService.postEmotionConversationStart()
            response.data?.roomId?.run {
                emit(DataState.Success(this))
            } ?: throw Throwable("getChatRoomId() failed, no room id received!")
        } catch (e: Exception) {
            emit(DataState.Error(e))
        } finally {
            emit(DataState.Loading(isLoading = false))
        }
    }

    override suspend fun connectChatRoom(roomId: String): Flow<DataState<Boolean>> = flow {
        // TODO("Not yet implemented")
        emit(DataState.Loading(isLoading = true))
        delay(3000)
        emit(DataState.Success(true))
        delay(1000)
        emit(DataState.Loading(isLoading = false))
    }

    override suspend fun disconnectChatRoom(roomId: String): Flow<DataState<Boolean>> = flow {
        // TODO("Not yet implemented")
        emit(DataState.Loading(isLoading = true))
        delay(3000)
        emit(DataState.Success(true))
        delay(1000)
        emit(DataState.Loading(isLoading = false))
    }

    override fun observeChatMessages(roomId: String): Flow<ChatMessage> = flow {
        // TODO("Not yet implemented")

        while (true) {
            delay(5000)
            emit(
                ChatMessage(
                    roomId = roomId,
                    source = ChatMessage.MessageSource.SERVER,
                    content = "test_message"
                )
            )
            delay(500)
            emit(
                ChatMessage(
                    roomId = roomId,
                    source = ChatMessage.MessageSource.SERVER,
                    content = "test_message"
                )
            )
            delay(500)
            emit(
                ChatMessage(
                    roomId = roomId,
                    source = ChatMessage.MessageSource.SERVER,
                    content = "test_message"
                )
            )
        }
    }

    override suspend fun sendChatMessage(
        roomId: String,
        chatMessage: ChatMessage
    ): Flow<DataState<Boolean>> = flow {
        // TODO("Not yet implemented")
        emit(DataState.Loading(isLoading = true))
        delay(3000)
        emit(DataState.Success(true))
        delay(1000)
        emit(DataState.Loading(isLoading = true))
    }
}