package com.emotionstorage.remote.di

import com.emotionstorage.data.dataSource.TimeCapsuleRemoteDataSource
import com.emotionstorage.remote.dataSourceImpl.TimeCapsuleRemoteDataSourceImpl
import com.emotionstorage.data.dataSource.UserRemoteDataSource
import com.emotionstorage.remote.datasourceImpl.UserRemoteDataSourceImpl
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
    abstract fun bindTimeCapsuleRemoteDataSource(impl: TimeCapsuleRemoteDataSourceImpl): TimeCapsuleRemoteDataSource
    abstract fun bindUserRemoteDataSource(userRemoteDataSourceImpl: UserRemoteDataSourceImpl): UserRemoteDataSource
}
