package com.emotionstorage.ai_chat.domain.repo

import kotlinx.coroutines.flow.Flow

interface AiChatIntroRepository {
    val introSeen: Flow<Boolean>

    suspend fun setIntroSeen(value: Boolean)
}
