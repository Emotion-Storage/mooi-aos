package com.emotionstorage.local.di

import com.emotionstorage.data.dataSource.ChatIntroPrefsLocalDataSource
import com.emotionstorage.data.dataSource.SessionLocalDataSource
import com.emotionstorage.data.dataSource.UserLocalDataSource
import com.emotionstorage.local.dataSourceImpl.ChatIntroPrefLocalDataSourceImpl
import com.emotionstorage.local.dataSourceImpl.SessionLocalDataSourceImpl
import com.emotionstorage.local.dataSourceImpl.UserLocalDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalDataSourceModule {
    @Binds
    @Singleton
    abstract fun bindUserLocalDataSource(
        userLocalDataSourceImpl: UserLocalDataSourceImpl
    ): UserLocalDataSource

    @Binds
    @Singleton
    abstract fun bindSessionLocalDataSource(
        sessionLocalDataSourceImpl: SessionLocalDataSourceImpl
    ): SessionLocalDataSource

    @Binds
    @Singleton
    abstract fun bindChatIntroPrefsLocalDataSource(
        chatIntroPrefLocalDataSourceImpl: ChatIntroPrefLocalDataSourceImpl
    ): ChatIntroPrefsLocalDataSource
}