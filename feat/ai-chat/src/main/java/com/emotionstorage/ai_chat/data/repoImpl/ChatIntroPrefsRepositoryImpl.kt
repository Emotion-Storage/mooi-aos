package com.emotionstorage.ai_chat.data.repoImpl

import com.emotionstorage.ai_chat.data.dataSource.local.ChatIntroPrefsLocalDataSource
import com.emotionstorage.ai_chat.domain.repo.ChatIntroPrefsRepository
import javax.inject.Inject

class ChatIntroPrefsRepositoryImpl @Inject constructor(
    private val localDataSource: ChatIntroPrefsLocalDataSource,
) : ChatIntroPrefsRepository {
    override val introSeen = localDataSource.introSeen

    override suspend fun setIntroSeen(value: Boolean) {
        localDataSource.setIntroSeen(value)
    }
}
