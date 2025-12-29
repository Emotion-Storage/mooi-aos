package com.emotionstorage.domain.useCase.chat

import com.emotionstorage.domain.repo.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveTempSaveRoomIdUseCase @Inject constructor(
    private val chatRepository: ChatRepository,
) {
    operator fun invoke() : Flow<Long?> = chatRepository.tempSavedRoomId
}
