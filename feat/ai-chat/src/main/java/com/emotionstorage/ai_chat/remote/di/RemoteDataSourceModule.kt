package com.emotionstorage.ai_chat.remote.di

import com.emotionstorage.ai_chat.data.dataSource.ChatRemoteDataSource
import com.emotionstorage.ai_chat.data.dataSource.ChatWSDataSource
import com.emotionstorage.ai_chat.remote.dataSource.ChatRemoteDataSourceImpl
import com.emotionstorage.ai_chat.remote.dataSource.ChatWSDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteDataSourceModule {
    @Binds
    @Singleton
    abstract fun bindChatRemoteDataSource(
        impl: ChatRemoteDataSourceImpl
    ): ChatRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindChatWSDataSource(
        impl: ChatWSDataSourceImpl
    ): ChatWSDataSource
}
