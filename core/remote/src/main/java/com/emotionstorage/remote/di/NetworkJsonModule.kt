package com.emotionstorage.remote.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkJsonModule {
    @Provides
    @Singleton
    fun provideJson() = Json {
        ignoreUnknownKeys = true // Common configuration
        isLenient = true
        prettyPrint = true // For debugging, optional
    }
}
