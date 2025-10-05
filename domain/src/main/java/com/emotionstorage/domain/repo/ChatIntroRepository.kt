package com.emotionstorage.domain.repo

import kotlinx.coroutines.flow.Flow

interface ChatIntroRepository {
    fun observeIntroSeen(): Flow<Boolean>

    suspend fun markIntroSeen(value: Boolean)
}
