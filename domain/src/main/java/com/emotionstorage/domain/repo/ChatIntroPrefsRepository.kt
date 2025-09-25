package com.emotionstorage.domain.repo

import kotlinx.coroutines.flow.Flow

interface ChatIntroPrefsRepository {
    val introSeen: Flow<Boolean>

    suspend fun setIntroSeen(value: Boolean)
}