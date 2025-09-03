package com.emotionstorage.ai_chat.domain.usecase

import com.emotionstorage.ai_chat.domain.model.ChatMessage
import com.emotionstorage.ai_chat.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveChatMessagesUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    operator fun invoke(roomId: String): Flow<ChatMessage> =
        chatRepository.observeChatMessages(roomId)
}