package com.emotionstorage.ai_chat.data.di

import com.emotionstorage.ai_chat.data.repoImpl.ChatRepositoryImpl
import com.emotionstorage.ai_chat.domain.repo.ChatRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindChatRepository(impl: ChatRepositoryImpl): ChatRepository
}