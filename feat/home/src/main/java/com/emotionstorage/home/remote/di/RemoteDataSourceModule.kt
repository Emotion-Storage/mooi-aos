package com.emotionstorage.home.remote.di

import com.emotionstorage.home.data.dataSource.HomeRemoteDataSource
import com.emotionstorage.home.remote.dataSourceImpl.HomeRemoteDataSourceImpl
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
    abstract fun bindHomeRemoteDataSource(
        impl: HomeRemoteDataSourceImpl
    ): HomeRemoteDataSource
}
