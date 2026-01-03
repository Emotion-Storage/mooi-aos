package com.emotionstorage.domain.useCase.chat

import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.model.EmotionConversationStartInfo
import com.emotionstorage.domain.repo.ChatRepository
import javax.inject.Inject

class GetChatRoomIdUseCase
    @Inject
    constructor(
        private val chatRepository: ChatRepository,
    ) {
        suspend operator fun invoke(): DataState<EmotionConversationStartInfo> =
            chatRepository
                .startEmotionConversation()
    }
