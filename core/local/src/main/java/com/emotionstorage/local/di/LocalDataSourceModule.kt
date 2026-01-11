package com.emotionstorage.local.di

import com.emotionstorage.data.dataSource.local.AiChatIntroLocalDataSource
import com.emotionstorage.data.dataSource.local.AttendanceClaimLocalDataSource
import com.emotionstorage.data.dataSource.local.FcmLocalDataSource
import com.emotionstorage.data.dataSource.local.NotificationPermissionLocalDataSource
import com.emotionstorage.data.dataSource.local.SessionLocalDataSource
import com.emotionstorage.data.dataSource.local.TimeCapsuleLocalDataSource
import com.emotionstorage.data.dataSource.local.UserLocalDataSource
import com.emotionstorage.data.repoImpl.AiChatIntroRepositoryImpl
import com.emotionstorage.domain.repo.ChatIntroRepository
import com.emotionstorage.local.dataSourceImpl.AiChatIntroLocalDataSourceImpl
import com.emotionstorage.local.dataSourceImpl.AttendanceClaimLocalDataSourceImpl
import com.emotionstorage.local.dataSourceImpl.FcmLocalDataSourceImpl
import com.emotionstorage.local.dataSourceImpl.NotificationPermissionLocalLocalDataSourceImpl
import com.emotionstorage.local.dataSourceImpl.SessionLocalDataSourceImpl
import com.emotionstorage.local.dataSourceImpl.TimeCapsuleLocalDataSourceImpl
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
    abstract fun bindFcmLocalDataSource(impl: FcmLocalDataSourceImpl): FcmLocalDataSource

    @Binds
    @Singleton
    abstract fun bindUserLocalDataSource(impl: UserLocalDataSourceImpl): UserLocalDataSource

    @Binds
    @Singleton
    abstract fun bindSessionLocalDataSource(impl: SessionLocalDataSourceImpl): SessionLocalDataSource

    @Binds
    @Singleton
    abstract fun bindTimeCapsuleLocalDataSource(impl: TimeCapsuleLocalDataSourceImpl): TimeCapsuleLocalDataSource

    @Binds
    @Singleton
    abstract fun bindNotificationPermissionDataSource(
        impl: NotificationPermissionLocalLocalDataSourceImpl,
    ): NotificationPermissionLocalDataSource

    @Binds
    @Singleton
    abstract fun bindAiChatIntroLocalDataSource(impl: AiChatIntroLocalDataSourceImpl): AiChatIntroLocalDataSource

    @Binds
    @Singleton
    abstract fun bindAiChatIntroRepository(impl: AiChatIntroRepositoryImpl): ChatIntroRepository

    @Binds
    @Singleton
    abstract fun bindAttendanceClaimLocalDataSource(
        impl: AttendanceClaimLocalDataSourceImpl,
    ): AttendanceClaimLocalDataSource
}
