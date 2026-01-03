package com.emotionstorage.domain.useCase.chat

import com.emotionstorage.domain.repo.ChatRepository
import javax.inject.Inject

class DeleteChatRoomUseCase @Inject constructor(
    private val chatRepository: ChatRepository,
) {
    suspend operator fun invoke(roomId: Long) = chatRepository.deleteChatRoom(roomId)
}
