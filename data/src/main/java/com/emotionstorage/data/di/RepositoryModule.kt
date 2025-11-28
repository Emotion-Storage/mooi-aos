package com.emotionstorage.data.di

import com.emotionstorage.data.repoImpl.AuthRepositoryImpl
import com.emotionstorage.data.repoImpl.AttendanceRepositoryImpl
import com.emotionstorage.data.repoImpl.HomeRepositoryImpl
import com.emotionstorage.data.repoImpl.DailyReportRepositoryImpl
import com.emotionstorage.data.repoImpl.FcmRepositoryImpl
import com.emotionstorage.data.repoImpl.MyPageRepositoryImpl
import com.emotionstorage.data.repoImpl.NotificationSettingsRepositoryImpl
import com.emotionstorage.data.repoImpl.SessionRepositoryImpl
import com.emotionstorage.data.repoImpl.TimeCapsuleRepositoryImpl
import com.emotionstorage.data.repoImpl.UserRepositoryImpl
import com.emotionstorage.domain.repo.AttendanceRepository
import com.emotionstorage.domain.repo.AuthRepository
import com.emotionstorage.domain.repo.DailyReportRepository
import com.emotionstorage.domain.repo.FcmRepository
import com.emotionstorage.domain.repo.HomeRepository
import com.emotionstorage.domain.repo.MyPageRepository
import com.emotionstorage.domain.repo.NotificationSettingRepository
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
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindHomeRepository(impl: HomeRepositoryImpl): HomeRepository

    @Binds
    @Singleton
    abstract fun bindAttendanceRepository(impl: AttendanceRepositoryImpl): AttendanceRepository

    @Binds
    @Singleton
    abstract fun bindFcmRepository(impl: FcmRepositoryImpl): FcmRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun bindSessionRepository(impl: SessionRepositoryImpl): SessionRepository

    @Binds
    @Singleton
    abstract fun bindTimeCapsuleRepository(impl: TimeCapsuleRepositoryImpl): TimeCapsuleRepository

    @Binds
    @Singleton
    abstract fun bindDailyReportRepository(impl: DailyReportRepositoryImpl): DailyReportRepository

    @Binds
    @Singleton
    abstract fun bindMyPageRepository(impl: MyPageRepositoryImpl): MyPageRepository

    @Binds
    @Singleton
    abstract fun bindNotificationSettingRepository(
        impl: NotificationSettingsRepositoryImpl,
    ): NotificationSettingRepository
}
