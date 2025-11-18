package com.emotionstorage.local.di

import com.emotionstorage.data.dataSource.local.AttendanceLocalDataSource
import com.emotionstorage.data.dataSource.local.FcmLocalDataSource
import com.emotionstorage.data.dataSource.local.SessionLocalDataSource
import com.emotionstorage.data.dataSource.local.TimeCapsuleLocalDataSource
import com.emotionstorage.data.dataSource.local.UserLocalDataSource
import com.emotionstorage.local.dataSourceImpl.AttendanceLocalDataSourceImpl
import com.emotionstorage.local.dataSourceImpl.FcmLocalDataSourceImpl
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
    abstract fun bindLocalDataSource(impl: AttendanceLocalDataSourceImpl): AttendanceLocalDataSource

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
}
