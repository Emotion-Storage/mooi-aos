package com.emotionstorage.domain.useCase.chat

import com.emotionstorage.domain.repo.ChatIntroRepository
import javax.inject.Inject

class MarkChatIntroSeenUseCase @Inject constructor(
    private val repository: ChatIntroRepository,
) {
    suspend operator fun invoke(value: Boolean) = repository.markIntroSeen(value)
}
