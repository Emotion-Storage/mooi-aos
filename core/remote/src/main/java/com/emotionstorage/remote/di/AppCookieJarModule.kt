package com.emotionstorage.remote.di

import com.emotionstorage.data.dataSource.local.SessionLocalDataSource
import com.emotionstorage.remote.cookieJar.AppCookieJar
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppCookieJarModule {
    @Singleton
    @Provides
    fun provideAppCookieJar(sessionLocalDataSource: SessionLocalDataSource) = AppCookieJar(sessionLocalDataSource)
}
