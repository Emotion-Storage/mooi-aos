package com.emotionstorage.domain.useCase.chat

import com.emotionstorage.domain.model.ChatMessage
import com.emotionstorage.domain.repo.ChatRepository
import com.emotionstorage.domain.common.DataState
import javax.inject.Inject

class SendChatMessageUseCase
    @Inject
    constructor(
        private val chatRepository: ChatRepository,
    ) {
        suspend operator fun invoke(
            roomId: Long,
            chatMessage: ChatMessage,
        ): DataState<Boolean> = chatRepository.sendChatMessage(roomId, chatMessage)
    }
