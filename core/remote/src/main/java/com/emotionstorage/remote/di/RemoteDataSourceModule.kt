package com.emotionstorage.remote.di


import com.emotionstorage.data.dataSource.DailyReportRemoteDataSource
import com.emotionstorage.data.dataSource.MyPageRemoteDataSource
import com.emotionstorage.data.dataSource.NotificationSettingRemoteDataSource
import com.emotionstorage.data.dataSource.TimeCapsuleRemoteDataSource
import com.emotionstorage.data.dataSource.UserRemoteDataSource
import com.emotionstorage.remote.dataSourceImpl.DailyReportRemoteDataSourceImpl
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
