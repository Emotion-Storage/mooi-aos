package com.emotionstorage.ai_chat.domain.usecase.local

import com.emotionstorage.ai_chat.domain.repo.AiChatIntroRepository
import javax.inject.Inject

class MarkAiChatIntroSeenUseCase @Inject constructor(
    private val repository: AiChatIntroRepository,
) {
    suspend operator fun invoke(value: Boolean) = repository.markIntroSeen(value)
}
