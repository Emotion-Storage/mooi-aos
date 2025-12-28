package com.emotionstorage.remote.di

import com.emotionstorage.remote.api.AttendanceApiService
import com.emotionstorage.remote.api.AuthApiService
import com.emotionstorage.remote.api.DailyReportApiService
import com.emotionstorage.remote.api.FcmApiService
import com.emotionstorage.remote.api.HomeApiService
import com.emotionstorage.remote.api.MyPageApiService
import com.emotionstorage.remote.api.TimeCapsuleApiService
import com.emotionstorage.remote.api.UserApiService
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
    fun provideHomeApiService(retrofit: Retrofit): HomeApiService = retrofit.create(HomeApiService::class.java)

    @Singleton
    @Provides
    fun provideFcmApiService(retrofit: Retrofit): FcmApiService = retrofit.create(FcmApiService::class.java)

    @Singleton
    @Provides
    fun provideTimeCapsuleApiService(retrofit: Retrofit): TimeCapsuleApiService =
        retrofit.create(TimeCapsuleApiService::class.java)

    @Singleton
    @Provides
    fun provideDailyReportApiService(retrofit: Retrofit): DailyReportApiService =
        retrofit.create(DailyReportApiService::class.java)

    @Provides
    @Singleton
    fun provideUserApiService(retrofit: Retrofit): UserApiService = retrofit.create(UserApiService::class.java)

    @Provides
    @Singleton
    fun provideMyPageApiService(retrofit: Retrofit): MyPageApiService = retrofit.create(MyPageApiService::class.java)

    @Provides
    @Singleton
    fun provideAttendanceApiService(retrofit: Retrofit): AttendanceApiService =
        retrofit.create(AttendanceApiService::class.java)
}
