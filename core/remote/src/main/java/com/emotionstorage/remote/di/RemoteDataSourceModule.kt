package com.emotionstorage.remote.di

import com.emotionstorage.data.dataSource.remote.DailyReportRemoteDataSource
import com.emotionstorage.data.dataSource.remote.FcmRemoteDataSource
import com.emotionstorage.data.dataSource.remote.HomeRemoteDataSource
import com.emotionstorage.data.dataSource.remote.MyPageRemoteDataSource
import com.emotionstorage.data.dataSource.remote.NotificationSettingRemoteDataSource
import com.emotionstorage.data.dataSource.remote.TimeCapsuleRemoteDataSource
import com.emotionstorage.data.dataSource.remote.UserRemoteDataSource
import com.emotionstorage.remote.dataSourceImpl.DailyReportRemoteDataSourceImpl
import com.emotionstorage.remote.dataSourceImpl.FcmRemoteDataSourceImpl
import com.emotionstorage.remote.dataSourceImpl.HomeRemoteDataSourceImpl
import com.emotionstorage.remote.dataSourceImpl.MyPageRemoteDataSourceImpl
import com.emotionstorage.remote.dataSourceImpl.NotificationSettingRemoteDataSourceImpl
import com.emotionstorage.remote.dataSourceImpl.TimeCapsuleRemoteDataSourceImpl
import com.emotionstorage.remote.dataSourceImpl.UserRemoteDataSourceImpl
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
    abstract fun bindHomeRemoteDataSource(impl: HomeRemoteDataSourceImpl): HomeRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindFcmRemoteDataSource(impl: FcmRemoteDataSourceImpl): FcmRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindTimeCapsuleRemoteDataSource(impl: TimeCapsuleRemoteDataSourceImpl): TimeCapsuleRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindDailyReportRemoteDataSource(impl: DailyReportRemoteDataSourceImpl): DailyReportRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindUserRemoteDataSource(impl: UserRemoteDataSourceImpl): UserRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindMyPageRemoteDataSource(impl: MyPageRemoteDataSourceImpl): MyPageRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindNotificationSettingRemoteDataSource(
        impl: NotificationSettingRemoteDataSourceImpl,
    ): NotificationSettingRemoteDataSource
}
