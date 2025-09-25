package com.emotionstorage.data.repoImpl

import com.emotionstorage.data.dataSource.ChatIntroPrefsLocalDataSource
import com.emotionstorage.domain.repo.ChatIntroPrefsRepository
import javax.inject.Inject

class ChatIntroPrefsRepositoryImpl @Inject constructor(
    private val localDataSource: ChatIntroPrefsLocalDataSource,
) : ChatIntroPrefsRepository {

    override val introSeen = localDataSource.introSeen

    override suspend fun setIntroSeen(value: Boolean) {
        localDataSource.setIntroSeen(value)
    }
}