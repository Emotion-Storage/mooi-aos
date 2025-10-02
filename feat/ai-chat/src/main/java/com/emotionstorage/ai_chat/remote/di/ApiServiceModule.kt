package com.emotionstorage.ai_chat.remote.di

import com.emotionstorage.ai_chat.remote.api.ChatApiService
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
    fun provideChatApiService(retrofit: Retrofit): ChatApiService = retrofit.create(ChatApiService::class.java)
}
