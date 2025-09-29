package com.emotionstorage.ai_chat.data.dataSource.local

import kotlinx.coroutines.flow.Flow

interface AiChatIntroLocalDataSource {
    fun observeIntroSeen(): Flow<Boolean>

    suspend fun markIntroSeen(value: Boolean)
}
