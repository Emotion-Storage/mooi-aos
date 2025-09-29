package com.emotionstorage.ai_chat.data.repoImpl

import com.emotionstorage.ai_chat.data.dataSource.local.AiChatIntroLocalDataSource
import com.emotionstorage.ai_chat.domain.repo.AiChatIntroRepository
import javax.inject.Inject

class AiChatIntroRepositoryImpl @Inject constructor(
    private val localDataSource: AiChatIntroLocalDataSource,
) : AiChatIntroRepository {
    override val introSeen = localDataSource.introSeen

    override suspend fun setIntroSeen(value: Boolean) {
        localDataSource.setIntroSeen(value)
    }
}
