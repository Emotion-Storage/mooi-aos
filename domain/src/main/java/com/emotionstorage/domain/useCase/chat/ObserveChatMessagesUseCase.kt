package com.emotionstorage.domain.useCase.chat

import com.emotionstorage.domain.model.ChatMessage
import com.emotionstorage.domain.repo.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveChatMessagesUseCase
    @Inject
    constructor(
        private val chatRepository: ChatRepository,
    ) {
        suspend operator fun invoke(roomId: String): Flow<ChatMessage> = chatRepository.observeChatMessages(roomId)
    }
