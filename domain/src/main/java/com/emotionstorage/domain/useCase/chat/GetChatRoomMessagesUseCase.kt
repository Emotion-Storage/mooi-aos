package com.emotionstorage.domain.useCase.chat

import com.emotionstorage.domain.repo.ChatRepository
import javax.inject.Inject

class GetChatRoomMessagesUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
//    suspend operator fun invoke(cursor: Long? = null) = chatRepository.getChatRoomMessages(cursor)
}
