package com.emotionstorage.home.data.di

import com.emotionstorage.domain.repo.AttendanceRepository
import com.emotionstorage.domain.repo.HomeRepository
import com.emotionstorage.home.data.repoImpl.AttendanceRepositoryImpl
import com.emotionstorage.home.data.repoImpl.HomeRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindHomeRepository(impl: HomeRepositoryImpl): HomeRepository

    @Binds
    @Singleton
    abstract fun bindAttendanceRepository(impl: AttendanceRepositoryImpl): AttendanceRepository
}
