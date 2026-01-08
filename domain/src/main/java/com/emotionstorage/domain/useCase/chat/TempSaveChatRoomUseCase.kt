package com.emotionstorage.domain.useCase.chat

import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.repo.ChatRepository
import javax.inject.Inject

class TempSaveChatRoomUseCase @Inject constructor(
    private val chatRepository: ChatRepository,
) {
    suspend operator fun invoke(roomId: Long): DataState<Long> = chatRepository.tempSaveChatRoom(roomId)
}
