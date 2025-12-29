package com.emotionstorage.domain.useCase.chat

import com.emotionstorage.domain.repo.ChatRepository
import javax.inject.Inject

class ClearTempSavedRoomIdUseCase @Inject constructor(
    private val repository: ChatRepository,
) {
    suspend operator fun invoke() {
        repository.clearTempSavedRoomId()
    }
}
