package com.emotionstorage.ai_chat.data.repoImpl

import com.emotionstorage.ai_chat.domain.model.ChatMessage
import com.emotionstorage.ai_chat.domain.repo.ChatRepository
import com.emotionstorage.domain.common.DataState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor() : ChatRepository {
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

        var i = 0
        while (true) {
            delay(2000)
            emit(
                ChatMessage(
                    roomId = roomId,
                    source = ChatMessage.MessageSource.SERVER,
                    content = "test_message_$i"
                )
            )
            ++i
        }
    }

    val messageErrorRate = 3
    var messageCount = 0
    override suspend fun sendChatMessage(
        roomId: String,
        chatMessage: ChatMessage
    ): Flow<DataState<Boolean>> = flow {
        // TODO("Not yet implemented")
        emit(DataState.Loading(isLoading = true))
        delay(3000)
        if(messageCount % messageErrorRate == 0) {
            emit(DataState.Error(Throwable("error")))
        }else {
            emit(DataState.Success(true))
        }
        ++messageCount
        delay(1000)
        emit(DataState.Loading(isLoading = true))
    }
}