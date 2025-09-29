package com.emotionstorage.ai_chat.data.dataSource.local

import kotlinx.coroutines.flow.Flow

interface ChatIntroPrefsLocalDataSource {
    val introSeen: Flow<Boolean>

    suspend fun setIntroSeen(value: Boolean)
}
