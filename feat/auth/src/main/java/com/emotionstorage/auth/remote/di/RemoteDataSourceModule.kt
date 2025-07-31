package com.emotionstorage.auth.remote.di

import com.emotionstorage.auth.data.dataSource.AuthRemoteDataSource
import com.emotionstorage.auth.data.dataSource.GoogleRemoteDataSource
import com.emotionstorage.auth.data.dataSource.KakaoRemoteDataSource
import com.emotionstorage.auth.remote.dataSourceImpl.AuthRemoteDataSourceImpl
import com.emotionstorage.auth.remote.dataSourceImpl.GoogleRemoteDataSourceImpl
import com.emotionstorage.auth.remote.dataSourceImpl.KakaoRemoteDataSourceImpl
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
    abstract fun bindAuthRemoteDataSource(
        authRemoteDataSourceImpl: AuthRemoteDataSourceImpl
    ): AuthRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindKakaoRemoteDataSource(
        kakaoRemoteDataSourceImpl: KakaoRemoteDataSourceImpl
    ): KakaoRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindGoogleRemoteDataSource(
        googleRemoteDataSourceImpl: GoogleRemoteDataSourceImpl
    ): GoogleRemoteDataSource
}