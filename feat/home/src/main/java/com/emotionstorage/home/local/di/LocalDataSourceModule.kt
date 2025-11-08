package com.emotionstorage.home.local.di

import com.emotionstorage.home.data.dataSource.AttendanceLocalDataSource
import com.emotionstorage.home.local.dataSourceImpl.AttendanceLocalDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalDataSourceModule {
    @Binds
    abstract fun bindLocalDataSource(impl: AttendanceLocalDataSourceImpl): AttendanceLocalDataSource
}
