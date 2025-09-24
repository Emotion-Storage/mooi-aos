package com.emotionstorage.ai_chat.domain.usecase

import com.emotionstorage.ai_chat.domain.repo.ChatRepository
import com.emotionstorage.domain.common.DataState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetChatRoomIdUseCase
    @Inject
    constructor(
        private val chatRepository: ChatRepository,
    ) {
        suspend operator fun invoke(): Flow<DataState<String>> {
            return chatRepository.getChatRoomId()
        }
    }
