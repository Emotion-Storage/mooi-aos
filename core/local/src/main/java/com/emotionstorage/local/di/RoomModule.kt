package com.emotionstorage.local.di

import android.content.Context
import androidx.room.Room
import com.emotionstorage.local.room.database.AppDatabase
import com.emotionstorage.local.room.database.AppDatabaseConstant
import com.emotionstorage.local.room.migration.Migration2to3
import com.emotionstorage.local.room.migration.Migration3to4
import com.emotionstorage.local.room.migration.Migration4to5
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object RoomModule {
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
    ): AppDatabase =
        Room
            .databaseBuilder(
                context,
                AppDatabase::class.java,
                AppDatabaseConstant.NAME,
            ).fallbackToDestructiveMigration(false)
            .addMigrations(Migration2to3, Migration3to4, Migration4to5)
            .build()

    @Provides
    @Singleton
    fun provideUserDao(appDatabase: AppDatabase) = appDatabase.userDao()

    @Provides
    @Singleton
    fun provideSessionDao(appDatabase: AppDatabase) = appDatabase.sessionDao()

    @Provides
    @Singleton
    fun provideTimeCapsuleDao(appDatabase: AppDatabase) = appDatabase.timeCapsuleDao()

    @Provides
    @Singleton
    fun provideTimeCapsuleRemoteKeyDao(appDatabase: AppDatabase) = appDatabase.timeCapsuleRemoteKeyDao()

    @Provides
    @Singleton
    fun provideFavoriteTimeCapsuleRemoteKeyDao(appDatabase: AppDatabase) = appDatabase.favoriteTimeCapsuleRemoteKeyDao()
}
