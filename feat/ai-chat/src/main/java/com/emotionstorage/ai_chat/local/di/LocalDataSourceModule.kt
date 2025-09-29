package com.emotionstorage.ai_chat.local.di

import com.emotionstorage.ai_chat.data.dataSource.local.AiChatIntroLocalDataSource
import com.emotionstorage.ai_chat.data.repoImpl.AiChatIntroRepositoryImpl
import com.emotionstorage.ai_chat.domain.repo.AiChatIntroRepository
import com.emotionstorage.ai_chat.local.dataSource.AiChatIntroLocalDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalDataSourceModule {

    @Binds
    abstract fun bindAiChatIntroLocalDataSource(
        aiChatIntroLocalDataSourceImpl: AiChatIntroLocalDataSourceImpl
    ): AiChatIntroLocalDataSource

    @Binds
    abstract fun bindAiChatIntroRepository(
        aiChatIntroRepositoryImpl: AiChatIntroRepositoryImpl
    ): AiChatIntroRepository
}
