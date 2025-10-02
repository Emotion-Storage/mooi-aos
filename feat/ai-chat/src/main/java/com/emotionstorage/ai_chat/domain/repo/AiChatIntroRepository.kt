package com.emotionstorage.ai_chat.domain.repo

import kotlinx.coroutines.flow.Flow

interface AiChatIntroRepository {
    fun observeIntroSeen(): Flow<Boolean>

    suspend fun markIntroSeen(value: Boolean)
}
