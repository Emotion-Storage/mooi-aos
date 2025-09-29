package com.emotionstorage.ai_chat.domain.usecase.local

import com.emotionstorage.ai_chat.domain.repo.AiChatIntroRepository
import javax.inject.Inject

class ObserveAiChatIntroSeenUseCase @Inject constructor(
    private val repository: AiChatIntroRepository
) {
    operator fun invoke() = repository.observeIntroSeen()
}
