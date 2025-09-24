package com.emotionstorage.ai_chat.domain.usecase

import com.emotionstorage.ai_chat.domain.model.ChatMessage
import com.emotionstorage.ai_chat.domain.repo.ChatRepository
import com.emotionstorage.domain.common.DataState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SendChatMessageUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(
        roomId: String,
        chatMessage: ChatMessage
    ): Flow<DataState<Boolean>> =
        chatRepository.sendChatMessage(roomId, chatMessage)
}
