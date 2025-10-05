package com.emotionstorage.ai_chat.data.repoImpl

import com.emotionstorage.ai_chat.data.dataSource.local.AiChatIntroLocalDataSource
import com.emotionstorage.domain.repo.ChatIntroRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AiChatIntroRepositoryImpl @Inject constructor(
    private val localDataSource: AiChatIntroLocalDataSource,
) : ChatIntroRepository {
    override fun observeIntroSeen(): Flow<Boolean> = localDataSource.observeIntroSeen()

    override suspend fun markIntroSeen(value: Boolean) {
        localDataSource.markIntroSeen(value)
    }
}
