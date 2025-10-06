package com.emotionstorage.data.di

import com.emotionstorage.data.repoImpl.SessionRepositoryImpl
import com.emotionstorage.data.repoImpl.TimeCapsuleRepositoryImpl
import com.emotionstorage.data.repoImpl.UserRepositoryImpl
import com.emotionstorage.domain.repo.SessionRepository
import com.emotionstorage.domain.repo.TimeCapsuleRepository
import com.emotionstorage.domain.repo.UserRepository
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
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun bindSessionRepository(impl: SessionRepositoryImpl): SessionRepository

    @Binds
    @Singleton
    abstract fun bindTimeCapsuleRepository(impl: TimeCapsuleRepositoryImpl): TimeCapsuleRepository
}
