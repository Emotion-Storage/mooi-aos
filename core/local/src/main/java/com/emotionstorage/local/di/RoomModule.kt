package com.emotionstorage.local.di

import android.content.Context
import androidx.room.Room
import com.emotionstorage.local.room.AppDatabase
import com.emotionstorage.local.room.AppDatabaseConstant
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
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabaseConstant.NAME,
        ).fallbackToDestructiveMigration(false).build()
    }

    @Provides
    @Singleton
    fun provideUserDao(appDatabase: AppDatabase) = appDatabase.userDao()

    @Provides
    @Singleton
    fun provideSessionDao(appDatabase: AppDatabase) = appDatabase.sessionDao()
}
