package com.emotionstorage.ai_chat.local.di

import com.emotionstorage.ai_chat.data.dataSource.local.AiChatIntroLocalDataSource
import com.emotionstorage.ai_chat.data.repoImpl.AiChatIntroRepositoryImpl
import com.emotionstorage.domain.repo.ChatIntroRepository
import com.emotionstorage.ai_chat.local.dataSource.AiChatIntroLocalDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalDataSourceModule {
    @Binds
    abstract fun bindAiChatIntroLocalDataSource(impl: AiChatIntroLocalDataSourceImpl): AiChatIntroLocalDataSource

    @Binds
    abstract fun bindAiChatIntroRepository(impl: AiChatIntroRepositoryImpl): ChatIntroRepository
}
