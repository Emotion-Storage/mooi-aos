package com.emotionstorage.ai_chat.domain.usecase

import com.emotionstorage.ai_chat.domain.repository.ChatRepository
import com.emotionstorage.domain.common.DataState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ConnectChatRoomUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(roomId: String): Flow<DataState<Boolean>> =
        chatRepository.connectChatRoom(roomId)
}