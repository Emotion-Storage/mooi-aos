package com.emotionstorage.local.di

import android.content.Context
import androidx.room.Room
import com.emotionstorage.local.room.database.AppDatabase
import com.emotionstorage.local.room.database.AppDatabaseConstant
import com.emotionstorage.local.room.migration.MIGRATION_2_3
import com.emotionstorage.local.room.migration.MIGRATION_3_4
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
            .addMigrations(MIGRATION_2_3, MIGRATION_3_4)
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
}
