package com.emotionstorage.remote.di

import com.emotionstorage.remote.api.TimeCapsuleApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiServiceModule {
    @Singleton
    @Provides
    fun provideTimeCapsuleApiService(retrofit: Retrofit): TimeCapsuleApiService =
        retrofit.create(TimeCapsuleApiService::class.java)
}
