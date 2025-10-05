package com.emotionstorage.domain.useCase.chat

import com.emotionstorage.domain.repo.ChatRepository
import com.emotionstorage.domain.common.DataState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DisconnectChatRoomUseCase
    @Inject
    constructor(
        private val chatRepository: ChatRepository,
    ) {
        suspend operator fun invoke(roomId: String): Flow<DataState<Boolean>> =
            chatRepository.disconnectChatRoom(roomId)
    }
