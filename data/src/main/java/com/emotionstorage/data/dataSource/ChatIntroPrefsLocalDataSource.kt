package com.emotionstorage.data.dataSource

import kotlinx.coroutines.flow.Flow

interface ChatIntroPrefsLocalDataSource {
    val introSeen: Flow<Boolean>

    suspend fun setIntroSeen(value: Boolean)
}
