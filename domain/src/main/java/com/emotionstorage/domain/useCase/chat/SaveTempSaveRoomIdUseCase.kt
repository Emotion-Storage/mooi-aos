package com.emotionstorage.domain.useCase.chat

import com.emotionstorage.domain.repo.ChatRepository
import javax.inject.Inject

class SaveTempSaveRoomIdUseCase @Inject constructor(
    private val repository: ChatRepository,
) {
    suspend operator fun invoke(roomId: Long) {
        repository.saveTempSavedRoomId(roomId)
    }
}
