package com.emotionstorage.ai_chat.data.dataSource.local

import kotlinx.coroutines.flow.Flow

interface AiChatIntroLocalDataSource {
    val introSeen: Flow<Boolean>

    suspend fun setIntroSeen(value: Boolean)
}
