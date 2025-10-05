package com.emotionstorage.domain.useCase.chat

import com.emotionstorage.domain.repo.ChatIntroRepository
import javax.inject.Inject

class ObserveChatIntroSeenUseCase @Inject constructor(
    private val repository: ChatIntroRepository,
) {
    operator fun invoke() = repository.observeIntroSeen()
}
